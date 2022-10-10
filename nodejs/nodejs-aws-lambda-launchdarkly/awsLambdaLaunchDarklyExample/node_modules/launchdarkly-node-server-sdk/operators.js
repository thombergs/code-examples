const semver = require('semver');

// Our reference SDK, Go, parses date/time strings with the time.RFC3339Nano format. This regex should match
// strings that are valid in that format, and no others.
// Acceptable: 2019-10-31T23:59:59Z, 2019-10-31T23:59:59.100Z, 2019-10-31T23:59:59-07, 2019-10-31T23:59:59-07:00, etc.
// Unacceptable: no "T", no time zone designation
const dateRegex = new RegExp('^\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d(\\.\\d\\d*)?(Z|[-+]\\d\\d(:\\d\\d)?)');

function stringOperator(f) {
  return (userValue, clauseValue) =>
    typeof userValue === 'string' && typeof clauseValue === 'string' && f(userValue, clauseValue);
}

function numericOperator(f) {
  return (userValue, clauseValue) =>
    typeof userValue === 'number' && typeof clauseValue === 'number' && f(userValue, clauseValue);
}

function dateOperator(f) {
  return (userValue, clauseValue) => {
    const userValueNum = parseDate(userValue);
    const clauseValueNum = parseDate(clauseValue);
    return userValueNum !== null && clauseValueNum !== null && f(userValueNum, clauseValueNum);
  };
}

function parseDate(input) {
  switch (typeof input) {
    case 'number':
      return input;
    case 'string':
      return dateRegex.test(input) ? Date.parse(input) : null;
    default:
      return null;
  }
}

function semVerOperator(fn) {
  return (a, b) => {
    const av = parseSemVer(a),
      bv = parseSemVer(b);
    return av && bv ? fn(av, bv) : false;
  };
}

function parseSemVer(input) {
  if (typeof input !== 'string') {
    return null;
  }
  if (input.startsWith('v')) {
    // the semver library tolerates a leading "v", but the standard does not.
    return null;
  }
  let ret = semver.parse(input);
  if (!ret) {
    const versionNumericComponents = new RegExp('^\\d+(\\.\\d+)?(\\.\\d+)?').exec(input);
    if (versionNumericComponents) {
      let transformed = versionNumericComponents[0];
      for (let i = 1; i < versionNumericComponents.length; i++) {
        if (versionNumericComponents[i] === undefined) {
          transformed = transformed + '.0';
        }
      }
      transformed = transformed + input.substring(versionNumericComponents[0].length);
      ret = semver.parse(transformed);
    }
  }
  return ret;
}

function safeRegexMatch(pattern, value) {
  try {
    return new RegExp(pattern).test(value);
  } catch (e) {
    // do not propagate this exception, just treat a bad regex as a non-match for consistency with other SDKs
    return false;
  }
}

const operators = {
  in: (a, b) => a === b,
  endsWith: stringOperator((a, b) => a.endsWith(b)),
  startsWith: stringOperator((a, b) => a.startsWith(b)),
  matches: stringOperator((a, b) => safeRegexMatch(b, a)),
  contains: stringOperator((a, b) => a.indexOf(b) > -1),
  lessThan: numericOperator((a, b) => a < b),
  lessThanOrEqual: numericOperator((a, b) => a <= b),
  greaterThan: numericOperator((a, b) => a > b),
  greaterThanOrEqual: numericOperator((a, b) => a >= b),
  before: dateOperator((a, b) => a < b),
  after: dateOperator((a, b) => a > b),
  semVerEqual: semVerOperator((a, b) => a.compare(b) === 0),
  semVerLessThan: semVerOperator((a, b) => a.compare(b) < 0),
  semVerGreaterThan: semVerOperator((a, b) => a.compare(b) > 0),
};

const operatorNone = () => false;

function fn(op) {
  return operators[op] || operatorNone;
}

module.exports = {
  operators: operators,
  fn: fn,
};
