const { nullLogger } = require('../loggers');
const dataKind = require('../versioned_data_kind');

const { runFeatureStoreTests } = require('./feature_store_tests');

const { promisifySingle, withCloseable } = require('launchdarkly-js-test-helpers');

// See index.d.ts for interface documentation

const cacheTime = 30;
const logger = nullLogger();

function runPersistentFeatureStoreTests(createStore, clearExistingData, createStoreWithConcurrentUpdateHook) {
  function doAllTestsWithPrefix(prefix) {
    describe('without cache', () => {
      runFeatureStoreTests(
        () => createStore(prefix, 0, logger),
        () => clearExistingData(prefix)
      );

      runPersistentFeatureStoreUncachedTests(prefix, createStore, clearExistingData);
    });

    describe('with cache', () => {
      runFeatureStoreTests(
        () => createStore(prefix, cacheTime, logger),
        () => clearExistingData(prefix)
      );

      // There are no special tests here that apply only when caching is enabled.
      // We are testing the cache behavior separately in caching_store_wrapper-test.
    });

    if (createStoreWithConcurrentUpdateHook) {
      describe('concurrent modification tests', () => {
        runPersistentFeatureStoreConcurrentUpdateTests(
          prefix,
          createStore,
          clearExistingData,
          createStoreWithConcurrentUpdateHook
        );
      });
    }
  }

  describe('with non-empty prefix', () => {
    doAllTestsWithPrefix('testprefix');
  });

  describe('with empty prefix', () => {
    doAllTestsWithPrefix(undefined);
  });

  runPersistentFeatureStoreSeparatePrefixesTest(createStore, clearExistingData);
}

function runPersistentFeatureStoreUncachedTests(prefix, createStore, clearExistingData) {
  const feature1 = { key: 'foo', version: 10 };

  async function testInitStateDetection(initData) {
    await clearExistingData(prefix);
    await withCloseable(createStore(prefix, 0, logger), async store1 => {
      await withCloseable(createStore(prefix, 0, logger), async store2 => {
        const result1 = await promisifySingle(store1.initialized)();
        expect(result1).toBe(false);

        await promisifySingle(store2.init)(initData);
        const result2 = await promisifySingle(store1.initialized)();
        expect(result2).toBe(true);
      });
    });
  }

  it('can detect if another instance has initialized the store', async () => {
    await testInitStateDetection({ features: { foo: feature1 } });
  });

  it('can detect if another instance has initialized the store, even with empty data', async () => {
    await testInitStateDetection({ features: {} });
  });
}

function runPersistentFeatureStoreSeparatePrefixesTest(createStore, clearExistingData) {
  it('is independent from other instances with different prefixes', async () => {
    const prefix1 = 'a';
    const prefix2 = 'b';
    const flag = { key: 'flag', version: 1 };
    await clearExistingData(prefix1);
    await clearExistingData(prefix2);
    await withCloseable(createStore(prefix1, 0, logger), async storeA => {
      await promisifySingle(storeA.init)({ features: { flag: flag } });
      await withCloseable(createStore(prefix2, 0, logger), async storeB => {
        await promisifySingle(storeB.init)({ features: {} });
        // create another instance just to make sure we're not reading cached data
        await withCloseable(createStore(prefix2, 0, logger), async storeB1 => {
          const item1 = await promisifySingle(storeB1.get)(dataKind.features, 'flag');
          expect(item1).toBe(null);
          const item2 = await promisifySingle(storeA.get)(dataKind.features, 'flag');
          expect(item2).toEqual(flag);
        });
      });
    });
  });
}

function runPersistentFeatureStoreConcurrentUpdateTests(
  prefix,
  createStore,
  clearExistingData,
  createStoreWithConcurrentUpdateHook
) {
  const flagKey = 'flag';
  const initialVersion = 1;

  function makeFlagWithVersion(v) {
    return { key: flagKey, version: v };
  }

  async function initStore(store) {
    const allData = { features: {} };
    allData['features'][flagKey] = makeFlagWithVersion(initialVersion);
    await promisifySingle(store.init)(allData);
  }

  function writeCompetingVersions(competingStore, flagVersionsToWrite) {
    let i = 0;
    return callback => {
      if (i < flagVersionsToWrite.length) {
        const newFlag = makeFlagWithVersion(flagVersionsToWrite[i]);
        i++;
        competingStore.upsert(dataKind.features, newFlag, callback);
      } else {
        callback();
      }
    };
  }

  it('handles upsert race condition against other client with lower version', async () => {
    await clearExistingData(prefix);
    await withCloseable(createStore(prefix, 0, logger), async competingStore => {
      const myDesiredVersion = 10;
      const competingStoreVersions = [2, 3, 4]; // proves that we can retry multiple times if necessary

      const myStore = createStoreWithConcurrentUpdateHook(
        prefix,
        logger,
        writeCompetingVersions(competingStore, competingStoreVersions)
      );
      await withCloseable(myStore, async myStore => {
        await initStore(myStore);
        await promisifySingle(myStore.upsert)(dataKind.features, makeFlagWithVersion(myDesiredVersion));
        const result = await promisifySingle(myStore.get)(dataKind.features, flagKey);
        expect(result.version).toEqual(myDesiredVersion);
      });
    });
  });

  it('handles upsert race condition against other client with higher version', async () => {
    await clearExistingData(prefix);
    await withCloseable(createStore(prefix, 0, logger), async competingStore => {
      const myDesiredVersion = 2;
      const competingStoreVersion = 3;

      const myStore = createStoreWithConcurrentUpdateHook(
        prefix,
        logger,
        writeCompetingVersions(competingStore, [competingStoreVersion])
      );
      await withCloseable(myStore, async myStore => {
        await initStore(myStore);
        await promisifySingle(myStore.upsert)(dataKind.features, makeFlagWithVersion(myDesiredVersion));
        const result = await promisifySingle(myStore.get)(dataKind.features, flagKey);
        expect(result.version).toEqual(competingStoreVersion);
      });
    });
  });
}

module.exports = {
  runPersistentFeatureStoreTests,
  runPersistentFeatureStoreUncachedTests,
  runPersistentFeatureStoreConcurrentUpdateTests,
};
