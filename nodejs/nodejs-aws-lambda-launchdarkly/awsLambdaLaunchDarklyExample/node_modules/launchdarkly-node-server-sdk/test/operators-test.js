var assert = require('assert');
var operators = require('../operators');

describe('operators', function() {
  const paramsTable = [
    // numeric comparisons
    [ 'in',                 99,      99,      true ],
    [ 'in',                 99.0001, 99.0001, true ],
    [ 'in',                 99,      99.0001, false ],
    [ 'in',                 99.0001, 99,      false ],
    [ 'lessThan',           99,      99.0001, true ],
    [ 'lessThan',           99.0001, 99,      false ],
    [ 'lessThan',           99,      99,      false ],
    [ 'lessThanOrEqual',    99,      99.0001, true ],
    [ 'lessThanOrEqual',    99.0001, 99,      false ],
    [ 'lessThanOrEqual',    99,      99,      true ],
    [ 'greaterThan',        99.0001, 99,      true ],
    [ 'greaterThan',        99,      99.0001, false ],
    [ 'greaterThan',        99,      99,      false ],
    [ 'greaterThanOrEqual', 99.0001, 99,      true ],
    [ 'greaterThanOrEqual', 99,      99.0001, false ],
    [ 'greaterThanOrEqual', 99,      99,      true ],

    // string comparisons
    [ 'in',         'x',   'x',   true ],
    [ 'in',         'x',   'xyz', false ],
    [ 'startsWith', 'xyz', 'x',   true ],
    [ 'startsWith', 'x',   'xyz', false ],
    [ 'endsWith',   'xyz', 'z',   true ],
    [ 'endsWith',   'z',   'xyz', false ],
    [ 'contains',   'xyz', 'y',   true ],
    [ 'contains',   'y',   'xyz', false ],

    // mixed strings and numbers
    [ 'in',                 '99', 99,   false ],
    [ 'in',                 99,   '99', false ],
    [ 'contains',           '99', 99,   false ],
    [ 'startsWith',         '99', 99,   false ],
    [ 'endsWith',           '99', 99,   false ],
    [ 'lessThanOrEqual',    '99', 99,   false ],
    [ 'lessThanOrEqual',    99,   '99', false ],
    [ 'greaterThanOrEqual', '99', 99,   false ],
    [ 'greaterThanOrEqual', 99,   '99', false ],

    // regex
    [ 'matches', 'hello world', 'hello.*rld',     true ],
    [ 'matches', 'hello world', 'hello.*rl',      true ],
    [ 'matches', 'hello world', 'l+',             true ],
    [ 'matches', 'hello world', '(world|planet)', true ],
    [ 'matches', 'hello world', 'aloha',          false ],
    [ 'matches', 'hello world', '***not a regex', false ],
    [ 'matches', 'hello world', 3,                false ],
    [ 'matches', 3,             'hello',          false ],

    // dates
    [ 'before', 0, 1,                              true ],
    [ 'before', -100, 0,                           true ],
    [ 'before', '1970-01-01T00:00:00Z', 1000,      true ],
    [ 'before', '1970-01-01T00:00:00.500Z', 1000,  true ],
    [ 'before', true, 1000,                        false ],  // wrong type
    [ 'after',  '1970-01-01T00:00:02.500Z', 1000,  true ],
    [ 'after',  '1970-01-01 00:00:02.500Z', 1000,  false ],  // malformed timestamp
    [ 'before', '1970-01-01T00:00:02+01:00', 1000, true ],
    [ 'before', -1000, 1000,                       true ],
    [ 'after',  '1970-01-01T00:00:01.001Z', 1000,  true ],
    [ 'after',  '1970-01-01T00:00:00-01:00', 1000, true ],

    // semver
    [ 'semVerEqual',       '2.0.1', '2.0.1',    true ],
    [ 'semVerEqual',       '2.0.1', '02.0.1',   false ],      // leading zeroes should be disallowed
    [ 'semVerEqual',       '2.0',   '2.0.0',    true ],
    [ 'semVerEqual',       '2',     '2.0.0',    true ],
    [ 'semVerEqual',       '2-rc1', '2.0.0-rc1', true ],
    [ 'semVerEqual',       '2+build2', '2.0.0+build2', true ],
    [ 'semVerEqual',       '2.0.0', '2.0.0+build2', true ],   // build metadata should be ignored in comparison
    [ 'semVerEqual',       '2.0.0', '2.0.0-rc1', false ],     // prerelease should not be ignored
    [ 'semVerEqual',       '2.0.0', '2.0.0+build_2', false ], // enforce allowable character set in build metadata
    [ 'semVerEqual',       '2.0.0', 'v2.0.0',   false ],      // disallow leading 'v'
    [ 'semVerLessThan',    '2.0.0', '2.0.1',    true ],
    [ 'semVerLessThan',    '2.0',   '2.0.1',    true ],
    [ 'semVerLessThan',    '2.0.1', '2.0.0',    false ],
    [ 'semVerLessThan',    '2.0.1', '2.0',      false ],
    [ 'semVerLessThan',    '2.0.0-rc', '2.0.0-rc.beta', true ],
    [ 'semVerLessThan',    '2.0.0-rc', '2.0.0', true ],
    [ 'semVerLessThan',    '2.0.0-rc.3', '2.0.0-rc.29', true ],
    [ 'semVerLessThan',    '2.0.0-rc.x29', '2.0.0-rc.x3', true ],
    [ 'semVerGreaterThan', '2.0.1', '2.0.0',    true ],
    [ 'semVerGreaterThan', '2.0.1', '2.0',      true ],
    [ 'semVerGreaterThan', '2.0.0', '2.0.1',    false ],
    [ 'semVerGreaterThan', '2.0',   '2.0.1',    false ],
    [ 'semVerGreaterThan', '2.0.0-rc.1', '2.0.0-rc.0', true ],
    [ 'semVerLessThan',    '2.0.1', 'xbad%ver', false ],
    [ 'semVerGreaterThan', '2.0.1', 'xbad%ver', false ]
  ];

  paramsTable.forEach(function(params) {
    it('result is ' + params[3] + ' for ' + JSON.stringify(params[1]) + ' ' + params[0] + ' ' +
      JSON.stringify(params[2]), function() {
      assert.equal(operators.fn(params[0])(params[1], params[2]), params[3]);
    });
  });
});
