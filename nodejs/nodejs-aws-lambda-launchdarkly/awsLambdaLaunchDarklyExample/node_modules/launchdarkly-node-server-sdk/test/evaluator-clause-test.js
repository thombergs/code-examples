const { Evaluator } = require('../evaluator');
const {
  eventFactory,
  makeBooleanFlagWithRules,
  makeBooleanFlagWithOneClause,
  asyncEvaluate,
  makeClauseThatMatchesUser,
} = require('./evaluator_helpers');

// Tests of flag evaluation at the clause level.

describe('Evaluator - clause', () => {
  it('coerces user key to string', async () => {
    const clause = { 'attribute': 'key', 'op': 'in', 'values': [ '999' ] };
    const flag = makeBooleanFlagWithOneClause(clause);
    const user = { 'key': 999 };
    const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, user, eventFactory);
    expect(detail.value).toBe(true);
  });

  it('coerces secondary key to string', async () => {
    // We can't really verify that the rollout calculation works correctly, but we can at least
    // make sure it doesn't error out if there's a non-string secondary value (ch35189)
    var rule = {
      id: 'ruleid',
      clauses: [
        { attribute: 'key', op: 'in', values: [ 'userkey' ] }
      ],
      rollout: {
        salt:  '',
        variations: [ { weight: 100000, variation: 1 } ]
      }
    };
    const flag = makeBooleanFlagWithRules([ rule ]);
    const user = { key: 'userkey', secondary: 999 };
    const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, user, eventFactory);
    expect(detail.value).toBe(true);
  });

  async function testClauseMatch(clause, user, shouldBe) {
    const flag = makeBooleanFlagWithOneClause(clause);
    const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, user, eventFactory);
    expect(detail.value).toBe(shouldBe);
  }

  it('can match built-in attribute', async () => {
    const user = { key: 'x', name: 'Bob' };
    const clause = { attribute: 'name', op: 'in', values: ['Bob'] };
    await testClauseMatch(clause, user, true);
  });

  it('can match custom attribute', async () => {
    const user = { key: 'x', name: 'Bob', custom: { legs: 4 } };
    const clause = { attribute: 'legs', op: 'in', values: [4] };
    await testClauseMatch(clause, user, true);
  });

  it('does not match missing attribute', async () => {
    const user = { key: 'x', name: 'Bob' };
    const clause = { attribute: 'legs', op: 'in', values: [4] };
    await testClauseMatch(clause, user, false);
  });

  it('can have a negated clause', async () => {
    const user = { key: 'x', name: 'Bob' };
    const clause = { attribute: 'name', op: 'in', values: ['Bob'], negate: true };
    await testClauseMatch(clause, user, false);
  });

  it('does not overflow the call stack when evaluating a huge number of clauses', async () => {
    const user = { key: 'user' };
    const clauseCount = 5000;
    const flag = {
      key: 'flag',
      targets: [],
      on: true,
      variations: [false, true],
      fallthrough: { variation: 0 }
    };
    // Note, for this test to be meaningful, the clauses must all match the user, since we
    // stop evaluating clauses on the first non-match.
    const clause = makeClauseThatMatchesUser(user);
    const clauses = [];
    for (var i = 0; i < clauseCount; i++) {
      clauses.push(clause);
    }
    var rule = { clauses: clauses, variation: 1 };
    flag.rules = [rule];
    const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, user, eventFactory);
    expect(err).toEqual(null);
    expect(detail.value).toEqual(true);
  });
});
