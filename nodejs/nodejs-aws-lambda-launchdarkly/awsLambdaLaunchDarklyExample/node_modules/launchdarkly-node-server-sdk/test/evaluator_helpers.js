const { EventFactory } = require('../event_factory');

const basicUser = { key: 'userkey' };
const eventFactory = EventFactory(false);

// Evaluator.evaluate uses a callback instead of a promise because it's a slightly more efficient
// way to pass multiple return values. But for the purposes of our tests, it's much easier to use
// a promise and async/await, so we'll transform it with this helper. Unlike usual Node promise
// semantics, here we treat "err" as just another return parameter rather than throwing an error
// (because the other parameters can still be non-null even if there's an error).
function asyncEvaluate(evaluator, flag, user, eventFactory) {
  return new Promise(resolve => {
    evaluator.evaluate(flag, user, eventFactory, (err, detail, events) => resolve([ err, detail, events ]));
  });
}

function makeFlagWithRules(rules, fallthrough) {
  if (!fallthrough) {
    fallthrough = { variation: 0 };
  }
  return {
    key: 'feature',
    on: true,
    rules: rules,
    targets: [],
    fallthrough: fallthrough,
    offVariation: 1,
    variations: ['a', 'b', 'c']
  };
}

function makeBooleanFlagWithRules(rules) {
  return {
    key: 'feature',
    on: true,
    prerequisites: [],
    rules: rules,
    targets: [],
    salt: '',
    fallthrough: { variation: 0 },
    offVariation: 0,
    variations: [ false, true ],
    version: 1
  };
}

function makeBooleanFlagWithOneClause(clause) {
  return makeBooleanFlagWithRules([ { clauses: [ clause ], variation: 1 } ]);
}

function makeFlagWithSegmentMatch(segment) {
  return makeBooleanFlagWithOneClause(makeSegmentMatchClause(segment));
}

function makeClauseThatMatchesUser(user) {
  return { attribute: 'key', op: 'in', values: [ user.key ] };
}

function makeClauseThatDoesNotMatchUser(user) {
  return { attribute: 'key', op: 'in', values: [ 'not-' + user.key ] };
}

function makeSegmentMatchClause(segment) {
  return { attribute: '', op: 'segmentMatch', values: [ segment.key ]};
}

function prepareQueries(data) {
  let flagsMap = {}, segmentsMap = {};
  for (const f of (data.flags || [])) {
    flagsMap[f.key] = f;
  }
  for (const s of (data.segments || [])) {
    segmentsMap[s.key] = s;
  }
  return {
    getFlag: (key, cb) => cb(flagsMap[key]),
    getSegment: (key, cb) => cb(segmentsMap[key]),
    getBigSegmentsMembership: (key, cb) => {
      if (data.bigSegments) {
        cb([ data.bigSegments[key], 'HEALTHY' ]);
      } else {
        cb(null);
      }
    },
  };
}

module.exports = {
  basicUser,
  eventFactory,
  asyncEvaluate,
  makeFlagWithRules,
  makeBooleanFlagWithRules,
  makeBooleanFlagWithOneClause,
  makeFlagWithSegmentMatch,
  makeClauseThatMatchesUser,
  makeClauseThatDoesNotMatchUser,
  makeSegmentMatchClause,
  prepareQueries,
};
