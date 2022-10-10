const { Evaluator, bucketUser } = require('../evaluator');
const {
  basicUser,
  eventFactory,
  prepareQueries,
  makeFlagWithRules,
  asyncEvaluate,
  makeClauseThatDoesNotMatchUser,
} = require('./evaluator_helpers');

// Tests of flag evaluation at the highest level. Rule-level and clause-level behavior is covered
// in detail in evaluator-rule-test, evaluator-clause-test, and evaluator-segment-match-test.

describe('Evaluator - basic flag behavior', () => {
  describe('flag is off', () => {
    it('returns off variation', async () => {
      const flag = {
        key: 'feature',
        on: false,
        offVariation: 1,
        fallthrough: { variation: 0 },
        variations: ['a', 'b', 'c']
      };
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, basicUser, eventFactory);
      expect(detail).toMatchObject({ value: 'b', variationIndex: 1, reason: { kind: 'OFF' } });
      expect(events).toBeUndefined();
    });

    it('returns null if off variation is unspecified', async () => {
      const flag = {
        key: 'feature',
        on: false,
        fallthrough: { variation: 0 },
        variations: ['a', 'b', 'c']
      };
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, basicUser, eventFactory);
      expect(detail).toMatchObject({ value: null, variationIndex: null, reason: { kind: 'OFF' } });
      expect(events).toBeUndefined();
    });

    it('returns error if off variation is too high', async () => {
      const flag = {
        key: 'feature',
        on: false,
        offVariation: 99,
        fallthrough: { variation: 0 },
        variations: ['a', 'b', 'c']
      };
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, basicUser, eventFactory);
      expect(err).toEqual(Error('Invalid variation index in flag'));
      expect(detail).toMatchObject({ value: null, variationIndex: null, reason: { kind: 'ERROR', errorKind: 'MALFORMED_FLAG' } });
      expect(events).toBeUndefined();
    });

    it('returns error if off variation is negative', async () => {
      const flag = {
        key: 'feature',
        on: false,
        offVariation: -1,
        fallthrough: { variation: 0 },
        variations: ['a', 'b', 'c']
      };
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, basicUser, eventFactory);
      expect(err).toEqual(Error('Invalid variation index in flag'));
      expect(detail).toMatchObject({ value: null, variationIndex: null, reason: { kind: 'ERROR', errorKind: 'MALFORMED_FLAG' } });
      expect(events).toBeUndefined();
    });
  });

  describe('fallthrough - flag is on and no rules match', () => {
    const noMatchClause = makeClauseThatDoesNotMatchUser(basicUser);

    it('returns fallthrough variation', async () => {
      var rule = { id: 'id', clauses: [noMatchClause], variation: 2 };
      const flag = makeFlagWithRules([rule], { variation: 0 });
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, basicUser, eventFactory);
      expect(detail).toMatchObject({ value: 'a', variationIndex: 0, reason: { kind: 'FALLTHROUGH' } });
      expect(events).toBeUndefined();
    });

    it('returns error if fallthrough variation is too high', async () => {
      var rule = { id: 'id', clauses: [noMatchClause], variation: 99 };
      const flag = makeFlagWithRules([rule], { variation: 99 });
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, basicUser, eventFactory);
      expect(err).toEqual(Error('Invalid variation index in flag'));
      expect(detail).toMatchObject({ value: null, variationIndex: null, reason: { kind: 'ERROR', errorKind: 'MALFORMED_FLAG' }});
      expect(events).toBeUndefined();
    });

    it('returns error if fallthrough variation is negative', async () => {
      var rule = { id: 'id', clauses: [noMatchClause], variation: 99 };
      const flag = makeFlagWithRules([rule], { variation: -1 });
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, basicUser, eventFactory);
      expect(err).toEqual(Error('Invalid variation index in flag'));
      expect(detail).toMatchObject({ value: null, variationIndex: null, reason: { kind: 'ERROR', errorKind: 'MALFORMED_FLAG' }});
      expect(events).toBeUndefined();
    });

    it('returns error if fallthrough has no variation or rollout', async () => {
      var rule = { id: 'id', clauses: [noMatchClause], variation: 99 };
      const flag = makeFlagWithRules([rule], { });
      var user = { key: 'x' };
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, basicUser, eventFactory);
      expect(err).toEqual(Error('Variation/rollout object with no variation or rollout'));
      expect(detail).toMatchObject({ value: null, variationIndex: null, reason: { kind: 'ERROR', errorKind: 'MALFORMED_FLAG' }});
      expect(events).toBeUndefined();
    });

    it('returns error if fallthrough has rollout with no variations', async () => {
      var rule = { id: 'id', clauses: [noMatchClause], variation: 99 };
      const flag = makeFlagWithRules([rule], { rollout: { variations: [] } });
      var user = { key: 'x' };
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, basicUser, eventFactory);
      expect(err).toEqual(Error('Variation/rollout object with no variation or rollout'));
      expect(detail).toMatchObject({ value: null, variationIndex: null, reason: { kind: 'ERROR', errorKind: 'MALFORMED_FLAG' }});
      expect(events).toBeUndefined();
    });
  });

  describe('prerequisites', () => {
    it('returns off variation if prerequisite is not found', async () => {
      const flag = {
        key: 'feature0',
        on: true,
        prerequisites: [{key: 'badfeature', variation: 1}],
        fallthrough: { variation: 0 },
        offVariation: 1,
        variations: ['a', 'b', 'c']
      };
      const e = Evaluator(prepareQueries({}));
      const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
      expect(detail).toMatchObject({ value: 'b', variationIndex: 1,
        reason: { kind: 'PREREQUISITE_FAILED', prerequisiteKey: 'badfeature' } });
      expect(events).toBeUndefined();
    });

    it('returns off variation and event if prerequisite is off', async () => {
      const flag = {
        key: 'feature0',
        on: true,
        prerequisites: [{key: 'feature1', variation: 1}],
        fallthrough: { variation: 0 },
        offVariation: 1,
        targets: [],
        rules: [],
        variations: ['a', 'b', 'c'],
        version: 1
      };
      const flag1 = {
        key: 'feature1',
        on: false,
        offVariation: 1,
        // note that even though it returns the desired variation, it is still off and therefore not a match
        fallthrough: { variation: 0 },
        targets: [],
        rules: [],
        variations: ['d', 'e'],
        version: 2
      };
      const e = Evaluator(prepareQueries({flags: [flag, flag1]}));
      const eventsShouldBe = [
        { kind: 'feature', key: 'feature1', variation: 1, value: 'e', version: 2, prereqOf: 'feature0' }
      ];
      const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
      expect(detail).toMatchObject({ value: 'b', variationIndex: 1,
        reason: { kind: 'PREREQUISITE_FAILED', prerequisiteKey: 'feature1' } });
      expect(events).toMatchObject(eventsShouldBe);
    });

    it('returns off variation and event if prerequisite is not met', async () => {
      const flag = {
        key: 'feature0',
        on: true,
        prerequisites: [{key: 'feature1', variation: 1}],
        fallthrough: { variation: 0 },
        offVariation: 1,
        targets: [],
        rules: [],
        variations: ['a', 'b', 'c'],
        version: 1
      };
      const flag1 = {
        key: 'feature1',
        on: true,
        fallthrough: { variation: 0 },
        targets: [],
        rules: [],
        variations: ['d', 'e'],
        version: 2
      };
      const e = Evaluator(prepareQueries({ flags: [flag, flag1] }));
      const eventsShouldBe = [
        { kind: 'feature', key: 'feature1', variation: 0, value: 'd', version: 2, prereqOf: 'feature0' }
      ];
      const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
      expect(detail).toMatchObject({ value: 'b', variationIndex: 1,
        reason: { kind: 'PREREQUISITE_FAILED', prerequisiteKey: 'feature1' } });
      expect(events).toMatchObject(eventsShouldBe);
    });

    it('returns fallthrough variation and event if prerequisite is met and there are no rules', async () => {
      const flag = {
        key: 'feature0',
        on: true,
        prerequisites: [{key: 'feature1', variation: 1}],
        fallthrough: { variation: 0 },
        offVariation: 1,
        targets: [],
        rules: [],
        variations: ['a', 'b', 'c'],
        version: 1
      };
      const flag1 = {
        key: 'feature1',
        on: true,
        fallthrough: { variation: 1 },
        targets: [],
        rules: [],
        variations: ['d', 'e'],
        version: 2
      };
      const e = Evaluator(prepareQueries({ flags: [flag, flag1] }))
      const eventsShouldBe = [
        { kind: 'feature', key: 'feature1', variation: 1, value: 'e', version: 2, prereqOf: 'feature0' }
      ];
      const [ err, detail, events ] = await asyncEvaluate(e, flag, basicUser, eventFactory);
      expect(detail).toMatchObject({ value: 'a', variationIndex: 0, reason: { kind: 'FALLTHROUGH' } });
      expect(events).toMatchObject(eventsShouldBe);
    });
  });

  describe('targets', () => {
    it('matches user from targets', async () => {
      const flag = {
        key: 'feature0',
        on: true,
        rules: [],
        targets: [
          {
            variation: 2,
            values: ['some', 'userkey', 'or', 'other']
          }
        ],
        fallthrough: { variation: 0 },
        offVariation: 1,
        variations: ['a', 'b', 'c']
      };
      const user = { key: 'userkey' };
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, user, eventFactory);
      expect(detail).toMatchObject({ value: 'c', variationIndex: 2, reason: { kind: 'TARGET_MATCH' } });
      expect(events).toBeUndefined();
    });
  });

  describe('rollout', () => {
    it('selects bucket', async () => {
      const user = { key: 'userkey' };
      const flagKey = 'flagkey';
      const salt = 'salt';

      // First verify that with our test inputs, the bucket value will be greater than zero and less than 100000,
      // so we can construct a rollout whose second bucket just barely contains that value
      const bucketValue = Math.floor(bucketUser(user, flagKey, 'key', salt) * 100000);
      expect(bucketValue).toBeGreaterThan(0);
      expect(bucketValue).toBeLessThan(100000);

      const badVariationA = 0, matchedVariation = 1, badVariationB = 2;
      const rollout = {
        variations: [
          { variation: badVariationA, weight: bucketValue }, // end of bucket range is not inclusive, so it will *not* match the target value
          { variation: matchedVariation, weight: 1 }, // size of this bucket is 1, so it only matches that specific value
          { variation: badVariationB, weight: 100000 - (bucketValue + 1) }
        ]
      };
      const flag = {
        key: flagKey,
        salt: salt,
        on: true,
        fallthrough: { rollout: rollout },
        variations: [ null, null, null ]
      };
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, user, eventFactory);
      expect(err).toEqual(null);
      expect(detail.variationIndex).toEqual(matchedVariation);
    });

    it('uses last bucket if bucket value is equal to total weight', async () => {
      const user = { key: 'userkey' };
      const flagKey = 'flagkey';
      const salt = 'salt';

      // We'll construct a list of variations that stops right at the target bucket value
      const bucketValue = Math.floor(bucketUser(user, flagKey, 'key', salt) * 100000);
      
      const rollout = {
        variations: [ { variation: 0, weight: bucketValue }]
      };
      const flag = {
        key: flagKey,
        salt: salt,
        on: true,
        fallthrough: { rollout: rollout },
        variations: [ null, null, null ]
      };
      const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, user, eventFactory);
      expect(err).toEqual(null);
      expect(detail.variationIndex).toEqual(0);
    });

    describe('with seed', () => {
      const seed = 61;
      const flagKey = 'flagkey';
      const salt = 'salt';
      const rollout = {
        kind: 'experiment',
        seed,
        variations: [
          { variation: 0, weight: 10000 },
          { variation: 1, weight: 20000 },
          { variation: 0, weight: 70000, untracked: true },
        ],
      };
      const flag = {
        key: flagKey,
        salt: salt,
        on: true,
        fallthrough: { rollout: rollout },
        variations: [null, null, null],
      };

      it('buckets user into first variant of the experiment', async () => {
        var user = { key: 'userKeyA' };
        const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, user, eventFactory);
        expect(err).toEqual(null);
        expect(detail.variationIndex).toEqual(0);
        expect(detail.reason.inExperiment).toBe(true);
      });

      it('uses seed to bucket user into second variant of the experiment', async () => {
        var user = { key: 'userKeyB' };
        const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, user, eventFactory);
        expect(err).toEqual(null);
        expect(detail.variationIndex).toEqual(1);
        expect(detail.reason.inExperiment).toBe(true);
      });

      it('buckets user outside of the experiment', async () => {
        var user = { key: 'userKeyC' };
        const [ err, detail, events ] = await asyncEvaluate(Evaluator(), flag, user, eventFactory);
        expect(err).toEqual(null);
        expect(detail.variationIndex).toEqual(0);
        expect(detail.reason.inExperiment).toBe(undefined);
      });
    });
  });
});

describe('bucketUser', () => {
  it('gets expected bucket values for specific keys', () => {
    var user = { key: 'userKeyA' };
    var bucket = bucketUser(user, 'hashKey', 'key', 'saltyA');
    expect(bucket).toBeCloseTo(0.42157587, 7);

    user = { key: 'userKeyB' };
    bucket = bucketUser(user, 'hashKey', 'key', 'saltyA');
    expect(bucket).toBeCloseTo(0.6708485, 7);

    user = { key: 'userKeyC' };
    bucket = bucketUser(user, 'hashKey', 'key', 'saltyA');
    expect(bucket).toBeCloseTo(0.10343106, 7);
  });

  it('can bucket by int value (equivalent to string)', () => {
    var user = {
      key: 'userKey',
      custom: {
        intAttr: 33333,
        stringAttr: '33333'
      }
    };
    var bucket = bucketUser(user, 'hashKey', 'intAttr', 'saltyA');
    var bucket2 = bucketUser(user, 'hashKey', 'stringAttr', 'saltyA');
    expect(bucket).toBeCloseTo(0.54771423, 7);
    expect(bucket2).toBe(bucket);
  });

  it('cannot bucket by float value', () => {
    var user = {
      key: 'userKey',
      custom: {
        floatAttr: 33.5
      }
    };
    var bucket = bucketUser(user, 'hashKey', 'floatAttr', 'saltyA');
    expect(bucket).toBe(0);
  });

  describe('when seed is present', () => {
    const seed = 61;
    it('gets expected bucket values for specific keys', () => {
      var user = { key: 'userKeyA' };
      var bucket = bucketUser(user, 'hashKey', 'key', 'saltyA', seed);
      expect(bucket).toBeCloseTo(0.09801207, 7);

      user = { key: 'userKeyB' };
      bucket = bucketUser(user, 'hashKey', 'key', 'saltyA', seed);
      expect(bucket).toBeCloseTo(0.14483777, 7);

      user = { key: 'userKeyC' };
      bucket = bucketUser(user, 'hashKey', 'key', 'saltyA', seed);
      expect(bucket).toBeCloseTo(0.9242641, 7);
    });

    it('should not generate a different bucket when hashKey or salt are changed', () => {
      let user = { key: 'userKeyA' };
      let bucket = bucketUser(user, 'hashKey', 'key', 'saltyA', seed);
      let bucketDifferentHashKey = bucketUser(user, 'otherHashKey', 'key', 'saltyA', seed);
      let bucketDifferentSalt = bucketUser(user, 'hashKey', 'key', 'otherSaltyA', seed);

      expect(bucketDifferentHashKey).toBeCloseTo(bucket, 7);
      expect(bucketDifferentSalt).toBeCloseTo(bucket, 7);
    });

    it('should generate a new bucket if the seed changes', () => {
      const otherSeed = 60;
      var user = { key: 'userKeyA' };
      var bucket = bucketUser(user, 'hashKey', 'key', 'saltyA', otherSeed);
      expect(bucket).toBeCloseTo(0.7008816, 7);
    });
  });
});
