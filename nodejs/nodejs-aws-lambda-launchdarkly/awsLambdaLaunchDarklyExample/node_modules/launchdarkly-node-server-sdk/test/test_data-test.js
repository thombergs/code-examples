const LDClient = require('../index');
const TestData = require('../test_data');
const InMemoryFeatureStore = require('../feature_store');
const dataKind = require('../versioned_data_kind');
const { promisify, promisifySingle } = require('launchdarkly-js-test-helpers');

describe('TestData', function() {
  it('initializes the datastore with flags configured before client is started', async function() {
    const td = TestData();
    td.update(td.flag('new-flag').variationForAllUsers(true));

    const store = InMemoryFeatureStore();
    const client = LDClient.init('sdk_key', { offline: true, featureStore: store, updateProcessor: td });

    await client.waitForInitialization();

    const res = await promisifySingle(store.all)(dataKind.features);
    expect(res).toEqual({
      'new-flag': {
        fallthrough: {
          variation: 0,
        },
        key: 'new-flag',
        offVariation: 1,
        on: true,
        variations: [ true, false ],
        version: 1
      }
    });
  });

  it('updates the datastore with its flags when update is called', async function() {
    const td = TestData();
    const store = InMemoryFeatureStore();
    const client = LDClient.init('sdk_key', { offline: true, featureStore: store, updateProcessor: td });

    await client.waitForInitialization();
    const res = await promisifySingle(store.all)(dataKind.features);
    expect(res).toEqual({});

    await td.update(td.flag('new-flag').variationForAllUsers(true));

    const postUpdateRes = await promisifySingle(store.all)(dataKind.features);
    expect(postUpdateRes).toEqual({
      'new-flag': {
        fallthrough: {
          variation: 0,
        },
        key: 'new-flag',
        offVariation: 1,
        on: true,
        variations: [ true, false ],
        version: 1
      }
    });
  });

  it('can include preconfigured items', async function() {
    const td = TestData();
    td.usePreconfiguredFlag({ key: 'my-flag', version: 1000, on: true });
    td.usePreconfiguredSegment({ key: 'my-segment', version: 2000 });

    const store = InMemoryFeatureStore();
    const client = LDClient.init('sdk_key', { offline: true, featureStore: store, updateProcessor: td });

    await client.waitForInitialization();

    const flags = await promisifySingle(store.all)(dataKind.features);
    expect(flags).toEqual({
      'my-flag': {
        key: 'my-flag',
        version: 1000,
        on: true,
      }
    });

    const segments = await promisifySingle(store.all)(dataKind.segments);
    expect(segments).toEqual({
      'my-segment': {
        key: 'my-segment',
        version: 2000
      }
    });

    td.usePreconfiguredFlag({ key: 'my-flag', on: false });

    const updatedFlag = await promisifySingle(store.get)(dataKind.features, 'my-flag');
    expect(updatedFlag).toEqual({
      key: 'my-flag',
      version: 1001,
      on: false,
    });

    td.usePreconfiguredSegment({ key: 'my-segment', included: [ 'x' ] });

    const updatedSegment = await promisifySingle(store.get)(dataKind.segments, 'my-segment');
    expect(updatedSegment).toEqual({
      key: 'my-segment',
      version: 2001,
      included: [ 'x' ],
    });
  });

  it('the datasource does not update the store after stop is called', async function() {
    const td = TestData();
    const store = InMemoryFeatureStore();
    const tds = td({featureStore: store});
    await promisifySingle(tds.start)();

    const res = await promisifySingle(store.all)(dataKind.features);
    expect(res).toEqual({});

    tds.stop();
    await td.update(td.flag('new-flag').variationForAllUsers(true));

    const postUpdateRes = await promisifySingle(store.all)(dataKind.features);
    expect(postUpdateRes).toEqual({});
  });

  it('can update a flag that exists in the store already', async function() {
    const td = TestData();
    const store = InMemoryFeatureStore();
    const tds = td({featureStore: store});
    await promisifySingle(tds.start)();

    await td.update(td.flag('new-flag').variationForAllUsers(true));
    const res = await promisifySingle(store.all)(dataKind.features);
    expect(res).toEqual({
      'new-flag': {
        fallthrough: {
          variation: 0,
        },
        key: 'new-flag',
        offVariation: 1,
        on: true,
        variations: [ true, false ],
        version: 1
      }
    });

    await td.update(td.flag('new-flag').variationForAllUsers(false));
    const res2 = await promisifySingle(store.all)(dataKind.features);
    expect(res2).toEqual({
      'new-flag': {
        fallthrough: {
          variation: 1,
        },
        key: 'new-flag',
        offVariation: 1,
        on: true,
        variations: [ true, false ],
        version: 2
      }
    });
  });

  it('should perform an immutable copy when TestData.flag is called after update', function() {
    const td = TestData();
    const flag = td.flag('test-flag');
    td.update(flag);
    const flag_copy = td.flag('test-flag');
    flag_copy.on(false);
    expect(flag_copy).not.toEqual(flag);
  });

  it('a new FlagBuilder defaults to on', function() {
    const td = TestData();
    expect(td.flag('whatever').build(0).on).toBe(true);
  });

  it('a new FlagBuilder defaults to boolean flag', function() {
    const td = TestData();
    const flag = td.flag('test-flag-booleanFlags');
    expect(flag.isBooleanFlag()).toBe(true)
    const flag2 = td.flag('test-flag-notBooleanFlags').valueForAllUsers('yes');
    expect(flag2.isBooleanFlag()).toBe(false)
  });

  it('FlagBuilder can set variations', function() {
    const td = TestData();
    const flag = td.flag('test-flag');
    flag.variations('a', 'b');
    expect(flag.build(0).variations).toEqual([ 'a', 'b' ]);
  });

  it('can handle boolean values for *Variation setters', function() {
    const td = TestData();
    const flag = td.flag('test-flag').fallthroughVariation(false);
    expect(flag.isBooleanFlag()).toBe(true);
    expect(flag.build(0).fallthrough).toEqual({variation: 1});

    const offFlag = td.flag('off-flag').offVariation(true);
    expect(offFlag.isBooleanFlag()).toBe(true);
    expect(offFlag.build(0).fallthrough).toEqual({variation: 0});
  });

  it('can set boolean values for a specific user target', function() {
    const td = TestData();
    const flag = td.flag('test-flag').variationForUser('ben', false);
    expect(flag.build(0).targets).toEqual([
      { variation: 1,
        values: ['ben']
      }
    ]);
    const clearedFlag = flag.copy().clearUserTargets();
    expect(clearedFlag.build(0)).not.toHaveProperty('targets');

  });

  it('can add and remove a rule', function() {
    const td = TestData();
    const flag = td.flag('test-flag')
                   .ifMatch('name', 'ben', 'christian')
                   .andNotMatch('country', 'fr')
                   .thenReturn(true);

    expect(flag.build().rules).toEqual([
      {
        "id": "rule0",
        "variation": 0,
        "clauses":  [
          {
            "attribute": "name",
            "negate": false,
            "operator": "in",
            "values":  [
              "ben",
              "christian",
            ],
          },
          {
            "attribute": "country",
            "negate": true,
            "operator": "in",
            "values":  [
              "fr",
            ],
          },
        ],
      }
    ]);

    const clearedRulesFlag = flag.clearRules();
    expect(clearedRulesFlag.build(0)).not.toHaveProperty('rules');
  });
});
