const crypto = require('crypto');

const operators = require('./operators');
const util = require('util');
const stringifyAttrs = require('./utils/stringifyAttrs');
const { safeAsyncEachSeries } = require('./utils/asyncUtils');

const builtins = ['key', 'secondary', 'ip', 'country', 'email', 'firstName', 'lastName', 'avatar', 'name', 'anonymous'];
const userAttrsToStringifyForEvaluation = ['key', 'secondary'];
// Currently we are not stringifying the rest of the built-in attributes prior to evaluation, only for events.
// This is because it could affect evaluation results for existing users (ch35206).

const noop = () => {};

// This internal object encapsulates SDK state that's used for every flag evaluation. Each
// LDClient maintains a single instance of it.
//
// The "queries" object provides read-only async data access on demand. Its methods are:
//   getFlag(key: string, callback: (flag) => void): void
//   getSegment(key: string, callback: (segment) => void): void
//   getBigSegmentsMembership(userKey: string, callback: ([ BigSegmentStoreMembership, status ]) => void): void
function Evaluator(queries) {
  const ret = {};

  ret.evaluate = (flag, user, eventFactory, maybeCallback) => {
    evaluate(flag, user, queries, eventFactory, maybeCallback);
  };

  return ret;
}

// Callback receives (err, detail, events) where detail has the properties "value", "variationIndex", and "reason";
// detail will never be null even if there's an error; events is either an array or undefined.
function evaluate(flag, user, queries, eventFactory, maybeCallback) {
  const cb = maybeCallback || noop;
  if (!user || user.key === null || user.key === undefined) {
    cb(null, errorResult('USER_NOT_SPECIFIED'), []);
    return;
  }

  if (!flag) {
    cb(null, errorResult('FLAG_NOT_FOUND'), []);
    return;
  }

  const sanitizedUser = stringifyAttrs(user, userAttrsToStringifyForEvaluation);
  const stateOut = {};
  evalInternal(flag, sanitizedUser, queries, stateOut, eventFactory, (err, detail) => {
    const result = detail;
    if (stateOut.bigSegmentsStatus) {
      result.reason.bigSegmentsStatus = stateOut.bigSegmentsStatus;
    }
    cb(err, result, stateOut.events);
  });
}

function evalInternal(flag, user, queries, stateOut, eventFactory, cb) {
  // If flag is off, return the off variation
  if (!flag.on) {
    getOffResult(flag, { kind: 'OFF' }, cb);
    return;
  }

  checkPrerequisites(flag, user, queries, stateOut, eventFactory, (err, failureReason) => {
    if (err || failureReason) {
      getOffResult(flag, failureReason, cb);
    } else {
      evalRules(flag, user, queries, stateOut, cb);
    }
  });
}

// Callback receives (err, reason) where reason is null if successful, or a "prerequisite failed" reason
function checkPrerequisites(flag, user, queries, stateOut, eventFactory, cb) {
  if (flag.prerequisites && flag.prerequisites.length) {
    safeAsyncEachSeries(
      flag.prerequisites,
      (prereq, callback) => {
        queries.getFlag(prereq.key, prereqFlag => {
          // If the flag does not exist in the store or is not on, the prerequisite
          // is not satisfied
          if (!prereqFlag) {
            callback({
              key: prereq.key,
              err: new Error('Could not retrieve prerequisite feature flag "' + prereq.key + '"'),
            });
            return;
          }
          evalInternal(prereqFlag, user, queries, stateOut, eventFactory, (err, detail) => {
            // If there was an error, the value is null, the variation index is out of range,
            // or the value does not match the indexed variation the prerequisite is not satisfied
            stateOut.events = stateOut.events || []; // eslint-disable-line no-param-reassign
            stateOut.events.push(eventFactory.newEvalEvent(prereqFlag, user, detail, null, flag));
            if (err) {
              callback({ key: prereq.key, err: err });
            } else if (!prereqFlag.on || detail.variationIndex !== prereq.variation) {
              // Note that if the prerequisite flag is off, we don't consider it a match no matter what its
              // off variation was. But we still evaluate it and generate an event.
              callback({ key: prereq.key });
            } else {
              // The prerequisite was satisfied
              callback(null);
            }
          });
        });
      },
      errInfo => {
        if (errInfo) {
          cb(errInfo.err, {
            kind: 'PREREQUISITE_FAILED',
            prerequisiteKey: errInfo.key,
          });
        } else {
          cb(null, null);
        }
      }
    );
  } else {
    cb(null, null);
  }
}

// Callback receives (err, detail)
function evalRules(flag, user, queries, stateOut, cb) {
  // Check target matches
  for (let i = 0; i < (flag.targets || []).length; i++) {
    const target = flag.targets[i];

    if (!target.values) {
      continue;
    }

    for (let j = 0; j < target.values.length; j++) {
      if (user.key === target.values[j]) {
        getVariation(flag, target.variation, { kind: 'TARGET_MATCH' }, cb);
        return;
      }
    }
  }

  safeAsyncEachSeries(
    flag.rules,
    (rule, callback) => {
      ruleMatchUser(rule, user, queries, stateOut, matched => {
        // We raise an "error" on the first rule that *does* match, to stop evaluating more rules
        callback(matched ? rule : null);
      });
    },
    // The following function executes once all of the rules have been checked
    err => {
      // we use the "error" value to indicate that a rule was successfully matched (since we only care
      // about the first match, and eachSeries terminates on the first "error")
      if (err) {
        const rule = err;
        const reason = { kind: 'RULE_MATCH', ruleId: rule.id };
        for (let i = 0; i < flag.rules.length; i++) {
          if (flag.rules[i].id === rule.id) {
            reason.ruleIndex = i;
            break;
          }
        }
        getResultForVariationOrRollout(rule, user, flag, reason, cb);
      } else {
        // no rule matched; check the fallthrough
        getResultForVariationOrRollout(flag.fallthrough, user, flag, { kind: 'FALLTHROUGH' }, cb);
      }
    }
  );
}

function ruleMatchUser(r, user, queries, stateOut, cb) {
  if (!r.clauses) {
    cb(false);
    return;
  }

  // A rule matches if all its clauses match.
  safeAsyncEachSeries(
    r.clauses,
    (clause, callback) => {
      clauseMatchUser(clause, user, queries, stateOut, matched => {
        // on the first clause that does *not* match, we raise an "error" to stop the loop
        callback(matched ? null : clause);
      });
    },
    err => {
      cb(!err);
    }
  );
}

function clauseMatchUser(c, user, queries, stateOut, cb) {
  if (c.op === 'segmentMatch') {
    safeAsyncEachSeries(
      c.values,
      (value, seriesCallback) => {
        queries.getSegment(value, segment => {
          if (segment) {
            segmentMatchUser(segment, user, queries, stateOut, result => {
              // On the first segment that matches, we call seriesCallback with an
              // arbitrary non-null value, which safeAsyncEachSeries interprets as an
              // "error", causing it to skip the rest of the series.
              seriesCallback(result ? segment : null);
            });
          } else {
            seriesCallback(null);
          }
        });
      },
      // The following function executes once all of the clauses have been checked
      err => {
        // an "error" indicates that a segment *did* match
        cb(maybeNegate(c, !!err));
      }
    );
  } else {
    cb(clauseMatchUserNoSegments(c, user));
  }
}

function clauseMatchUserNoSegments(c, user) {
  const uValue = userValue(user, c.attribute);

  if (uValue === null || uValue === undefined) {
    return false;
  }

  const matchFn = operators.fn(c.op);

  // The user's value is an array
  if (Array === uValue.constructor) {
    for (let i = 0; i < uValue.length; i++) {
      if (matchAny(matchFn, uValue[i], c.values)) {
        return maybeNegate(c, true);
      }
    }
    return maybeNegate(c, false);
  }

  return maybeNegate(c, matchAny(matchFn, uValue, c.values));
}

function segmentMatchUser(segment, user, queries, stateOut, cb) {
  if (!user.key) {
    return cb(false);
  }

  if (!segment.unbounded) {
    return cb(simpleSegmentMatchUser(segment, user, true));
  }

  if (!segment.generation) {
    // Big Segment queries can only be done if the generation is known. If it's unset,
    // that probably means the data store was populated by an older SDK that doesn't know
    // about the generation property and therefore dropped it from the JSON data. We'll treat
    // that as a "not configured" condition.
    stateOut.bigSegmentsStatus = 'NOT_CONFIGURED'; // eslint-disable-line no-param-reassign
    return cb(false);
  }

  if (stateOut.bigSegmentsStatus) {
    // We've already done the query at some point during the flag evaluation and stored
    // the result (if any) in stateOut.bigSegmentsMembership, so we don't need to do it
    // again. Even if multiple Big Segments are being referenced, the membership includes
    // *all* of the user's segment memberships.
    return cb(bigSegmentMatchUser(stateOut.bigSegmentsMembership, segment, user));
  }

  queries.getBigSegmentsMembership(user.key, result => {
    if (result) {
      stateOut.bigSegmentsMembership = result[0]; // eslint-disable-line no-param-reassign
      stateOut.bigSegmentsStatus = result[1]; // eslint-disable-line no-param-reassign
    } else {
      stateOut.bigSegmentsStatus = 'NOT_CONFIGURED'; // eslint-disable-line no-param-reassign
    }
    return cb(bigSegmentMatchUser(stateOut.bigSegmentsMembership, segment, user));
  });
}

function bigSegmentMatchUser(membership, segment, user) {
  const segmentRef = makeBigSegmentRef(segment);
  const included = membership && membership[segmentRef];
  if (included !== undefined) {
    return included;
  }
  return simpleSegmentMatchUser(segment, user, false);
}

function simpleSegmentMatchUser(segment, user, useIncludesAndExcludes) {
  if (useIncludesAndExcludes) {
    if ((segment.included || []).indexOf(user.key) >= 0) {
      return true;
    }
    if ((segment.excluded || []).indexOf(user.key) >= 0) {
      return false;
    }
  }
  for (let i = 0; i < (segment.rules || []).length; i++) {
    if (segmentRuleMatchUser(segment.rules[i], user, segment.key, segment.salt)) {
      return true;
    }
  }
}

function segmentRuleMatchUser(rule, user, segmentKey, salt) {
  for (let i = 0; i < (rule.clauses || []).length; i++) {
    if (!clauseMatchUserNoSegments(rule.clauses[i], user)) {
      return false;
    }
  }

  // If the weight is absent, this rule matches
  if (rule.weight === undefined || rule.weight === null) {
    return true;
  }

  // All of the clauses are met. See if the user buckets in
  const bucket = bucketUser(user, segmentKey, rule.bucketBy || 'key', salt);
  const weight = rule.weight / 100000.0;
  return bucket < weight;
}

function maybeNegate(c, b) {
  if (c.negate) {
    return !b;
  } else {
    return b;
  }
}

function matchAny(matchFn, value, values) {
  for (let i = 0; i < values.length; i++) {
    if (matchFn(value, values[i])) {
      return true;
    }
  }

  return false;
}

function getVariation(flag, index, reason, cb) {
  if (index === null || index === undefined || index < 0 || index >= flag.variations.length) {
    cb(new Error('Invalid variation index in flag'), errorResult('MALFORMED_FLAG'));
  } else {
    cb(null, { value: flag.variations[index], variationIndex: index, reason: reason });
  }
}

function getOffResult(flag, reason, cb) {
  if (flag.offVariation === null || flag.offVariation === undefined) {
    cb(null, { value: null, variationIndex: null, reason: reason });
  } else {
    getVariation(flag, flag.offVariation, reason, cb);
  }
}

function getResultForVariationOrRollout(r, user, flag, reason, cb) {
  if (!r) {
    cb(new Error('Fallthrough variation undefined'), errorResult('MALFORMED_FLAG'));
  } else {
    const [index, inExperiment] = variationForUser(r, user, flag);
    if (index === null || index === undefined) {
      cb(new Error('Variation/rollout object with no variation or rollout'), errorResult('MALFORMED_FLAG'));
    } else {
      const transformedReason = reason;
      if (inExperiment) {
        transformedReason.inExperiment = true;
      }
      getVariation(flag, index, transformedReason, cb);
    }
  }
}

function errorResult(errorKind) {
  return { value: null, variationIndex: null, reason: { kind: 'ERROR', errorKind: errorKind } };
}

// Given a variation or rollout 'r', select the variation for the given user.
// Returns an array of the form [variationIndex, inExperiment].
function variationForUser(r, user, flag) {
  if (r.variation !== null && r.variation !== undefined) {
    // This represets a fixed variation; return it
    return [r.variation, false];
  }
  const rollout = r.rollout;
  if (rollout) {
    const isExperiment = rollout.kind === 'experiment';
    const variations = rollout.variations;
    if (variations && variations.length > 0) {
      // This represents a percentage rollout. Assume
      // we're rolling out by key
      const bucketBy = rollout.bucketBy || 'key';
      const bucket = bucketUser(user, flag.key, bucketBy, flag.salt, rollout.seed);
      let sum = 0;
      for (let i = 0; i < variations.length; i++) {
        const variate = variations[i];
        sum += variate.weight / 100000.0;
        if (bucket < sum) {
          return [variate.variation, isExperiment && !variate.untracked];
        }
      }

      // The user's bucket value was greater than or equal to the end of the last bucket. This could happen due
      // to a rounding error, or due to the fact that we are scaling to 100000 rather than 99999, or the flag
      // data could contain buckets that don't actually add up to 100000. Rather than returning an error in
      // this case (or changing the scaling, which would potentially change the results for *all* users), we
      // will simply put the user in the last bucket.
      const lastVariate = variations[variations.length - 1];
      return [lastVariate.variation, isExperiment && !lastVariate.untracked];
    }
  }

  return [null, false];
}

// Fetch an attribute value from a user object. Automatically
// navigates into the custom array when necessary
function userValue(user, attr) {
  if (builtins.indexOf(attr) >= 0 && Object.hasOwnProperty.call(user, attr)) {
    return user[attr];
  }
  if (user.custom && Object.hasOwnProperty.call(user.custom, attr)) {
    return user.custom[attr];
  }
  return null;
}

// Compute a percentile for a user
function bucketUser(user, key, attr, salt, seed) {
  let idHash = bucketableStringValue(userValue(user, attr));

  if (idHash === null) {
    return 0;
  }

  if (user.secondary) {
    idHash += '.' + user.secondary;
  }

  const prefix = seed ? util.format('%d.', seed) : util.format('%s.%s.', key, salt);
  const hashKey = prefix + idHash;
  const hashVal = parseInt(sha1Hex(hashKey).substring(0, 15), 16);

  return hashVal / 0xfffffffffffffff;
}

function bucketableStringValue(value) {
  if (typeof value === 'string') {
    return value;
  }
  if (Number.isInteger(value)) {
    return '' + value;
  }
  return null;
}

function sha1Hex(input) {
  const hash = crypto.createHash('sha1');
  hash.update(input);
  return hash.digest('hex');
}

function makeBigSegmentRef(segment) {
  // The format of Big Segment references is independent of what store implementation is being
  // used; the store implementation receives only this string and does not know the details of
  // the data model. The Relay Proxy will use the same format when writing to the store.
  return segment.key + '.g' + segment.generation;
}

module.exports = {
  Evaluator,
  bucketUser,
  makeBigSegmentRef,
};
