
const { Evaluator } = require('../evaluator');
const {
  basicUser,
  eventFactory,
  prepareQueries,
  makeFlagWithSegmentMatch,
  asyncEvaluate,
  makeClauseThatMatchesUser,
  makeClauseThatDoesNotMatchUser,
} = require('./evaluator_helpers');

// Tests of flag evaluation at the segment-matching level - for simple segments, not big segments.

describe('Evaluator - segment match', () => {
  const matchClause = makeClauseThatMatchesUser(basicUser);

  it('matches segment with explicitly included user', async () => {
    const segment = {
      key: 'test',
      included: [ basicUser.key ],
      version: 1
    };
    const e = Evaluator(prepareQueries({ segments: [segment] }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(true);
  });

  it('does not match segment with explicitly excluded user', async () => {
    const segment = {
      key: 'test',
      excluded: [ basicUser.key ],
      version: 1
    };
    const e = Evaluator(prepareQueries({ segments: [segment] }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(false);
  });

  it('does not match segment with unknown user', async () => {
    const segment = {
      key: 'test',
      included: [ 'foo' ],
      version: 1
    };
    const e = Evaluator(prepareQueries({ segments: [segment] }));
    const flag = makeFlagWithSegmentMatch(segment);
    const user = { key: 'bar' };
    const [ err, detail, events ] = await asyncEvaluate(e, flag, user, eventFactory);
    expect(detail.value).toBe(false);
  });

  it('matches segment with user who is both included and excluded', async () => {
    const segment = {
      key: 'test',
      included: [ basicUser.key ],
      excluded: [ basicUser.key ],
      version: 1
    };
    const e = Evaluator(prepareQueries({ segments: [segment] }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(true);
  });

  it('matches segment with rule with full rollout', async () => {
    const segment = {
      key: 'test',
      rules: [
        {
          clauses: [ matchClause ],
          weight: 100000
        }
      ],
      version: 1
    };
    const e = Evaluator(prepareQueries({ segments: [segment] }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(true);
  });

  it('does not match segment with rule with zero rollout', async () => {
    const segment = {
      key: 'test',
      rules: [
        {
          clauses: [ matchClause ],
          weight: 0
        }
      ],
      version: 1
    };
    const e = Evaluator(prepareQueries({ segments: [segment] }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(false);
  });

  it('matches segment with multiple matching clauses', async () => {

    const user = { key: 'foo', email: 'test@example.com', name: 'bob' };
    const segment = {
      key: 'test',
      rules: [
        {
          clauses: [
            {
              attribute: 'email',
              op: 'in',
              values: [ user.email ]
            },
            {
              attribute: 'name',
              op: 'in',
              values: [ user.name ]
            }
          ]
        }
      ],
      version: 1
    };
    const e = Evaluator(prepareQueries({ segments: [segment] }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, user, eventFactory);
    expect(detail.value).toBe(true);
  });

  it('does not match segment if one clause does not match', async () => {
    const user = { key: 'foo', email: 'test@example.com', name: 'bob' };
    const segment = {
      key: 'test',
      rules: [
        {
          clauses: [ makeClauseThatMatchesUser(user), makeClauseThatDoesNotMatchUser(user) ],
        }
      ],
      version: 1
    };
    const e = Evaluator(prepareQueries({ segments: [segment] }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, user, eventFactory);
    expect(detail.value).toBe(false);
  });
});
