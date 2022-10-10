const { Evaluator, makeBigSegmentRef } = require('../evaluator');
const {
  basicUser,
  eventFactory,
  asyncEvaluate,
  makeFlagWithSegmentMatch,
  makeClauseThatMatchesUser,
  prepareQueries,
  makeSegmentMatchClause,
} = require('./evaluator_helpers');

// Tests of flag evaluation involving Big Segments.

describe('Evaluator - Big Segments', () => {
  it('segment is not matched if there is no way to query it', async () => {
    const segment = {
      key: 'test',
      included: [ basicUser.key ], // included should be ignored for a big segment
      version: 1,
      unbounded: true,
      generation: 1,
    };
    const e = Evaluator(prepareQueries({ segments: [segment] }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(false);
    expect(detail.reason.bigSegmentsStatus).toEqual('NOT_CONFIGURED');
  });

  it('segment with no generation is not matched', async () => {
    const segment = {
      key: 'test',
      included: [ basicUser.key ], // included should be ignored for a big segment
      version: 1,
      unbounded: true,
    };
    const e = Evaluator(prepareQueries({ segments: [segment] }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(false);
    expect(detail.reason.bigSegmentsStatus).toEqual('NOT_CONFIGURED');
  });

  it('matched with include', async () => {
    const segment = {
      key: 'test',
      version: 1,
      unbounded: true,
      generation: 2,
    };
    const membership = { [makeBigSegmentRef(segment)]: true };
    const e = Evaluator(prepareQueries({ segments: [segment], bigSegments: { [basicUser.key]: membership } }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(true);
    expect(detail.reason.bigSegmentsStatus).toEqual('HEALTHY');
  });

  it('matched with rule', async () => {
    const segment = {
      key: 'test',
      version: 1,
      unbounded: true,
      generation: 2,
      rules: [
        { clauses: makeClauseThatMatchesUser(basicUser) },
      ]
    };
    const membership = {};
    const e = Evaluator(prepareQueries({ segments: [segment], bigSegments: { [basicUser.key]: membership } }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(true);
    expect(detail.reason.bigSegmentsStatus).toEqual('HEALTHY');
  });

  it('unmatched by exclude regardless of rule', async () => {
    const segment = {
      key: 'test',
      version: 1,
      unbounded: true,
      generation: 2,
      rules: [
        { clauses: makeClauseThatMatchesUser(basicUser) },
      ]
    };
    const membership = { [makeBigSegmentRef(segment)]: false };
    const e = Evaluator(prepareQueries({ segments: [segment], bigSegments: { [basicUser.key]: membership } }));
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(false);
    expect(detail.reason.bigSegmentsStatus).toEqual('HEALTHY');
  });

  it('status is returned from provider', async () => {
    const segment = {
      key: 'test',
      version: 1,
      unbounded: true,
      generation: 2,
    };
    const membership = { [makeBigSegmentRef(segment)]: true };
    const queries = prepareQueries({ segments: [segment] });
    queries.getBigSegmentsMembership = (key, cb) => {
      cb([ membership, 'STALE' ]);
    };
    const e = Evaluator(queries);
    const flag = makeFlagWithSegmentMatch(segment);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(true);
    expect(detail.reason.bigSegmentsStatus).toEqual('STALE');
  });

  it('queries state only once per user even if flag references multiple segments', async () => {
    const segment1 = {
      key: 'segmentkey1',
      version: 1,
      unbounded: true,
      generation: 2,
    };
    const segment2 = {
      key: 'segmentkey2',
      version: 1,
      unbounded: true,
      generation: 3,
    };
    const flag = {
      key: "key",
      on: "true",
      fallthrough: { variation: 0 },
      variations: [ false, true ],
      rules: [
        { variation: 1, clauses: [ makeSegmentMatchClause(segment1) ]},
        { variation: 1, clauses: [ makeSegmentMatchClause(segment2) ]},
      ],
    }

    const membership = { [makeBigSegmentRef(segment2)]: true };
    // The membership deliberately does not include segment1, because we want the first rule to be
    // a non-match so that it will continue on and check segment2 as well.

    const queries = prepareQueries({ segments: [segment1, segment2] });
    let userQueryCount = 0;
    queries.getBigSegmentsMembership = (key, cb) => {
      userQueryCount++;
      cb([ membership, 'HEALTHY' ]);
    };

    const e = Evaluator(queries);
    const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
    expect(detail.value).toBe(true);
    expect(detail.reason.bigSegmentsStatus).toEqual('HEALTHY');

    expect(userQueryCount).toEqual(1);
  });
});
