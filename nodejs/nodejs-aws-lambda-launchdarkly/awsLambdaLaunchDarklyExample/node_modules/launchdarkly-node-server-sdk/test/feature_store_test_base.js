var dataKind = require('../versioned_data_kind');
const { runFeatureStoreTests } = require('../sharedtest/feature_store_tests');
const {
  runPersistentFeatureStoreUncachedTests,
  runPersistentFeatureStoreConcurrentUpdateTests,
} = require('../sharedtest/persistent_feature_store_tests');

// This file contains obsolete entry points with somewhat different semantics for the
// standard test suites in sharedtest/store_tests. It is retained here because older versions
// of the database integration packages reference this file directly, even though it was
// never documented. It isn't referenced by the SDK's own tests, and can be removed in the
// next major version.

// Parameters:
// - makeStore(): creates an instance of the feature store
// - clearExistingData(callback): if specified, will be called before each test to clear any
// storage that the store instances may be sharing; this also implies that the feature store
// - isCached: true if the instances returned by makeStore() have caching enabled.
// - makeStoreWithPrefix(prefix): creates an uncached instance of the store with a key prefix
function baseFeatureStoreTests(makeStore, clearExistingData, isCached, makeStoreWithPrefix) {
  if (clearExistingData) {
    // We're testing a persistent feature store implementation.
    const asyncClearExistingData = () => new Promise(resolve => clearExistingData(resolve));
    runFeatureStoreTests(
      makeStore,
      asyncClearExistingData,
    );
    if (!isCached) {
      runPersistentFeatureStoreUncachedTests(
        (prefix, cacheTTL, logger) => makeStorePrefix ? makeStoreWithPrefix(prefix) : makeStore(),
        asyncClearExistingData,
      );
    }
  } else {
    // We're testing an in-memory store or some other nonstandard implementation that doesn't
    // have shared-database semantics.
    runFeatureStoreTests(
      makeStore,
      () => new Promise(resolve => clearExistingData(resolve)),
    );
  }
}

// Parameters:
// - makeStore(): creates a normal feature store.
// - makeStoreWithHook(hook): creates a feature store that operates on the same underlying data as
// the first store. This store will call the hook function (passing a callback) immediately before
// it attempts to make any update.

function concurrentModificationTests(makeStore, makeStoreWithHook) {
  runPersistentFeatureStoreConcurrentUpdateTests(
    prefix => makeStore(),
    (prefix, hook) => makeStoreWithHook(hook),
  )
}

module.exports = {
  baseFeatureStoreTests: baseFeatureStoreTests,
  concurrentModificationTests: concurrentModificationTests
};
