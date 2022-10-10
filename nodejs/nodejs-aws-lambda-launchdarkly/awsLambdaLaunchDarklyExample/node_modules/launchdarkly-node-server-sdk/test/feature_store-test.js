const InMemoryFeatureStore = require('../feature_store');
const dataKind = require('../versioned_data_kind');
const { runFeatureStoreTests } = require('../sharedtest/feature_store_tests');
const stubs = require('./stubs');
const { promisifySingle } = require('launchdarkly-js-test-helpers');

describe('InMemoryFeatureStore', () => {
  runFeatureStoreTests(
    () => new InMemoryFeatureStore(),
  );
});

describe('custom feature store in configuration', () => {
  const defaultUser = { key: 'user' };

  async function makeStoreWithFlag() {
    const store = new InMemoryFeatureStore();
    const flag = { key: 'flagkey', on: false, offVariation: 0, variations: [ true ] };
    const data = {};
    data[dataKind.features.namespace] = { 'flagkey': flag };
    await promisifySingle(store.init)(data);
    return store;
  }

  it('can be specified as an instance', async () => {
    const store = await makeStoreWithFlag();
    const config = { featureStore: store };
    const client = stubs.createClient(config);
    await client.waitForInitialization();
    const result = await client.variation('flagkey', defaultUser, false);
    expect(result).toEqual(true);
  });

  it('can be specified as a factory function', async () => {
    const store = await makeStoreWithFlag();
    const config = { featureStore: () => store };
    const client = stubs.createClient(config);
    await client.waitForInitialization();
    const result = await client.variation('flagkey', defaultUser, false);
    expect(result).toEqual(true);
  });
})