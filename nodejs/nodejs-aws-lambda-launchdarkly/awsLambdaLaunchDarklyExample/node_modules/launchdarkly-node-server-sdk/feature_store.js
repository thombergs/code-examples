// The default in-memory implementation of a feature store, which holds feature flags and
// other related data received from LaunchDarkly.
//
// Other implementations of the same interface can be used by passing them in the featureStore
// property of the client configuration (that's why the interface here is async, even though
// the in-memory store doesn't do anything asynchronous - because other implementations may
// need to be async). The interface is defined by LDFeatureStore in index.d.ts.
//
// Additional implementations should use CachingStoreWrapper if possible.

// Note that the contract for feature store methods does *not* require callbacks to be deferred
// with setImmediate, process.nextTick, etc. It is both allowed and desirable to call them
// directly whenever possible (i.e. if we don't actually have to do any I/O), since otherwise
// feature flag retrieval is a major performance bottleneck. These methods are for internal use
// by the SDK, and the SDK does not make any assumptions about whether a callback executes
// before or after the next statement.

function InMemoryFeatureStore() {
  let allData = {};
  let initCalled = false;

  const store = {};

  function callbackResult(cb, result) {
    cb && cb(result);
  }

  store.get = (kind, key, cb) => {
    const items = allData[kind.namespace] || {};
    if (Object.hasOwnProperty.call(items, key)) {
      const item = items[key];

      if (!item || item.deleted) {
        callbackResult(cb, null);
      } else {
        callbackResult(cb, item);
      }
    } else {
      callbackResult(cb, null);
    }
  };

  store.all = (kind, cb) => {
    const results = {};
    const items = allData[kind.namespace] || {};

    for (const [key, item] of Object.entries(items)) {
      if (item && !item.deleted) {
        results[key] = item;
      }
    }

    callbackResult(cb, results);
  };

  store.init = (newData, cb) => {
    allData = newData;
    initCalled = true;
    callbackResult(cb);
  };

  store.delete = (kind, key, version, cb) => {
    let items = allData[kind.namespace];
    if (!items) {
      items = {};
      allData[kind] = items;
    }
    const deletedItem = { version: version, deleted: true };
    if (Object.hasOwnProperty.call(items, key)) {
      const old = items[key];
      if (!old || old.version < version) {
        items[key] = deletedItem;
      }
    } else {
      items[key] = deletedItem;
    }

    callbackResult(cb);
  };

  store.upsert = (kind, item, cb) => {
    const key = item.key;
    let items = allData[kind.namespace];
    if (!items) {
      items = {};
      allData[kind.namespace] = items;
    }

    if (Object.hasOwnProperty.call(items, key)) {
      const old = items[key];
      if (old && old.version < item.version) {
        items[key] = clone(item);
      }
    } else {
      items[key] = clone(item);
    }

    callbackResult(cb);
  };

  store.initialized = cb => {
    callbackResult(cb, initCalled === true);
  };

  store.close = () => {
    // Close on the in-memory store is a no-op
  };

  store.description = 'memory';

  return store;
}

// Deep clone an object. Does not preserve any
// functions on the object
function clone(obj) {
  return JSON.parse(JSON.stringify(obj));
}

module.exports = InMemoryFeatureStore;
