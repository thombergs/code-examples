var CachingStoreWrapper = require('../caching_store_wrapper');
var features = require('../versioned_data_kind').features;
var segments = require('../versioned_data_kind').segments;
const { promisifySingle, sleepAsync } = require('launchdarkly-js-test-helpers');

function MockCore() {
  const c = {
    data: { features: {} },
    inited: false,
    initQueriedCount: 0,
    getAllError: false,
    upsertError: null,
    closed: false,

    initInternal: function(newData, cb) { 
      c.data = newData;
      cb();
    },

    getInternal: function(kind, key, cb)  {
      cb(c.data[kind.namespace][key]);
    },

    getAllInternal: function(kind, cb) {
      cb(c.getAllError ? null : c.data[kind.namespace]);
    },

    upsertInternal: function(kind, item, cb) {
      if (c.upsertError) {
        cb(c.upsertError, null);
        return;
      }
      const oldItem = c.data[kind.namespace][item.key];
      if (oldItem && oldItem.version >= item.version) {
        cb(null, oldItem);
      } else {
        c.data[kind.namespace][item.key] = item;
        cb(null, item);
      }
    },

    initializedInternal: function(cb) {
      c.initQueriedCount++;
      cb(c.inited);
    },

    close: function() {
      c.closed = true;
    },

    forceSet: function(kind, item) {
      c.data[kind.namespace][item.key] =  item;
    },

    forceRemove: function(kind, key) {
      delete c.data[kind.namespace][key];
    }
  };
  return c;
}

function MockOrderedCore() { 
  const c = {
    data: { features: {} },

    initOrderedInternal: function(newData, cb) { 
      c.data = newData;
      cb();
    },
    // don't bother mocking the rest of the stuff since the wrapper behaves identically except for init
  };
  return c;
}

const cacheSeconds = 15;

function runCachedAndUncachedTests(name, testFn, coreFn) {
  var makeCore = coreFn ? coreFn : MockCore;
  describe(name, function() {
    const core1 = makeCore();
    const wrapper1 = new CachingStoreWrapper(core1, cacheSeconds);
    it('cached', async () => await testFn(wrapper1, core1, true), 1000);

    const core2 = makeCore();
    const wrapper2 = new CachingStoreWrapper(core2, 0);
    it('uncached', async () => await testFn(wrapper2, core2, false), 1000);
  });
}

function runCachedTestOnly(name, testFn, coreFn) {
  var makeCore = coreFn ? coreFn : MockCore;
  it(name, async () => {
    const core = makeCore();
    const wrapper = new CachingStoreWrapper(core, cacheSeconds);
    await testFn(wrapper, core);
  });
}

describe('CachingStoreWrapper', function() {

  runCachedAndUncachedTests('get()', async (wrapper, core, isCached) => {
    const flagv1 = { key: 'flag', version: 1 };
    const flagv2 = { key: 'flag', version: 2 };

    core.forceSet(features, flagv1);

    var item = await promisifySingle(wrapper.get)(features, flagv1.key);
    expect(item).toEqual(flagv1);

    core.forceSet(features, flagv2); // Make a change that bypasses the cache

    item = await promisifySingle(wrapper.get)(features, flagv1.key);
    // If cached, it should return the cached value rather than calling the underlying getter
    expect(item).toEqual(isCached ? flagv1 : flagv2);
  });

  runCachedAndUncachedTests('get() with deleted item', async (wrapper, core, isCached) => {
    const flagv1 = { key: 'flag', version: 1, deleted: true };
    const flagv2 = { key: 'flag', version: 2, deleted: false };

    core.forceSet(features, flagv1);

    var item = await promisifySingle(wrapper.get)(features, flagv1.key);
    expect(item).toBe(null);

    core.forceSet(features, flagv2); // Make a change that bypasses the cache

    item = await promisifySingle(wrapper.get)(features, flagv2.key);
    // If cached, the deleted state should persist in the cache
    expect(item).toEqual(isCached ? null : flagv2);
  });

  runCachedAndUncachedTests('get() with missing item', async (wrapper, core, isCached) => {
    const flag = { key: 'flag', version: 1 };

    var item = await promisifySingle(wrapper.get)(features, flag.key);
    expect(item).toBe(null);

    core.forceSet(features, flag);

    item = await promisifySingle(wrapper.get)(features, flag.key);
    // If cached, the previous null result should persist in the cache
    expect(item).toEqual(isCached ? null  : flag);
  });

  runCachedTestOnly('cached get() uses values from init()', async (wrapper, core) => {
    const flagv1 = { key: 'flag', version: 1 };
    const flagv2 = { key: 'flag', version: 2 };

    const allData = { features: { 'flag': flagv1 } };

    await promisifySingle(wrapper.init)(allData);
    expect(core.data).toEqual(allData);

    core.forceSet(features, flagv2);

    var item = await promisifySingle(wrapper.get)(features, flagv1.key);
    expect(item).toEqual(flagv1);
  });

  runCachedAndUncachedTests('all()', async (wrapper, core, isCached) => {
    const flag1 = { key: 'flag1', version: 1 };
    const flag2 = { key: 'flag2', version: 1 };

    core.forceSet(features, flag1);
    core.forceSet(features, flag2);

    var items = await promisifySingle(wrapper.all)(features);
    expect(items).toEqual({ 'flag1': flag1, 'flag2': flag2 });

    core.forceRemove(features, flag2.key);

    items = await promisifySingle(wrapper.all)(features);
    if (isCached) {
      expect(items).toEqual({ 'flag1': flag1, 'flag2': flag2 });
    } else {
      expect(items).toEqual({ 'flag1': flag1 });
    }
  });

  runCachedAndUncachedTests('all() with deleted item', async (wrapper, core, isCached) => {
    const flag1 = { key: 'flag1', version: 1 };
    const flag2 = { key: 'flag2', version: 1, deleted: true };

    core.forceSet(features, flag1);
    core.forceSet(features, flag2);

    var items = await promisifySingle(wrapper.all)(features);
    expect(items).toEqual({ 'flag1': flag1 });

    core.forceRemove(features, flag1.key);

    items = await promisifySingle(wrapper.all)(features);
    if (isCached) {
      expect(items).toEqual({ 'flag1': flag1 });
    } else {
      expect(items).toEqual({ });
    }
  });

  runCachedAndUncachedTests('all() error condition', async (wrapper, core, isCached) => {
    core.getAllError = true;

    var items = await promisifySingle(wrapper.all)(features);
    expect(items).toBe(null);
  });

  runCachedTestOnly('cached all() uses values from init()', async (wrapper, core) => {
    const flag1 = { key: 'flag1', version: 1 };
    const flag2 = { key: 'flag2', version: 1 };

    const allData = { features: { flag1: flag1, flag2: flag2 } };

    await promisifySingle(wrapper.init)(allData);
    core.forceRemove(features, flag2.key);

    var items = await promisifySingle(wrapper.all)(features);
    expect(items).toEqual({ flag1: flag1, flag2: flag2 });
  });

  runCachedTestOnly('cached all() uses fresh values if there has been an update', async (wrapper, core) => {
    const flag1v1 = { key: 'flag1', version: 1 };
    const flag1v2 = { key: 'flag1', version: 2 };
    const flag2v1 = { key: 'flag2', version: 1 };
    const flag2v2 = { key: 'flag2', version: 2 };

    const allData = { features: { flag1: flag1v1, flag2: flag2v2 } };

    await promisifySingle(wrapper.init)(allData);
    expect(core.data).toEqual(allData);

    // make a change to flag1 using the wrapper - this should flush the cache
    await promisifySingle(wrapper.upsert)(features, flag1v2);
    // make a change to flag2 that bypasses the cache
    core.forceSet(features, flag2v2);

    // we should now see both changes since the cache was flushed
    var items = await promisifySingle(wrapper.all)(features);
    expect(items).toEqual({ flag1: flag1v2, flag2: flag2v2 });
  });

  runCachedAndUncachedTests('upsert() - successful', async (wrapper, core, isCached) => {
    const flagv1 = { key: 'flag', version: 1 };
    const flagv2 = { key: 'flag', version: 2 };

    await promisifySingle(wrapper.upsert)(features, flagv1);
    expect(core.data[features.namespace][flagv1.key]).toEqual(flagv1);

    await promisifySingle(wrapper.upsert)(features, flagv2);
    expect(core.data[features.namespace][flagv1.key]).toEqual(flagv2);

    // if we have a cache, verify that the new item is now cached by writing a different value
    // to the underlying data - get() should still return the cached item
    if (isCached) {
      const flagv3 = { key: 'flag', version: 3 };
      core.forceSet(features, flagv3);
    }

    var item = await promisifySingle(wrapper.get)(features, flagv1.key);
    expect(item).toEqual(flagv2);
  });

  runCachedAndUncachedTests('upsert() - error', async (wrapper, core, isCached) => {
    const flagv1 = { key: 'flag', version: 1 };
    const flagv2 = { key: 'flag', version: 2 };

    await promisifySingle(wrapper.upsert)(features, flagv1);
    expect(core.data[features.namespace][flagv1.key]).toEqual(flagv1);

    core.upsertError = new Error('sorry');

    await promisifySingle(wrapper.upsert)(features, flagv2);
    expect(core.data[features.namespace][flagv1.key]).toEqual(flagv1);

    // if we have a cache, verify that the old item is still cached by writing a different value
    // to the underlying data - get() should still return the cached item
    if (isCached) {
      const flagv3 = { key: 'flag', version: 3 };
      core.forceSet(features, flagv3);
      var item = await promisifySingle(wrapper.get)(features, flagv1.key);
      expect(item).toEqual(flagv1);  
    }
  });

  runCachedTestOnly('cached upsert() - unsuccessful', async (wrapper, core) => {
    const flagv1 = { key: 'flag', version: 1 };
    const flagv2 = { key: 'flag', version: 2 };

    core.forceSet(features, flagv2); // this is now in the underlying data, but not in the cache

    await promisifySingle(wrapper.upsert)(features, flagv1);
    expect(core.data[features.namespace][flagv1.key]).toEqual(flagv2); // value in store remains the same

    // the cache should now contain flagv2 - check this by making another change that bypasses
    // the cache, and verifying that get() uses the cached value instead
    const flagv3 = { key: 'flag', version: 3 };
    core.forceSet(features, flagv3);

    var item = await promisifySingle(wrapper.get)(features, flagv1.key);
    expect(item).toEqual(flagv2);
  });

  runCachedAndUncachedTests('delete()', async (wrapper, core, isCached) => {
    const flagv1 = { key: 'flag', version: 1 };
    const flagv2 = { key: 'flag', version: 2, deleted: true };
    const flagv3 = { key: 'flag', version: 3 };

    core.forceSet(features, flagv1);

    var item = await promisifySingle(wrapper.get)(features, flagv1.key);
    expect(item).toEqual(flagv1);

    await promisifySingle(wrapper.delete)(features, flagv1.key, flagv2.version);

    expect(core.data[features.namespace][flagv1.key]).toEqual(flagv2);

    // make a change to the flag that bypasses the cache
    core.forceSet(features, flagv3);

    var item = await promisifySingle(wrapper.get)(features, flagv1.key);
    expect(item).toEqual(isCached ? null : flagv3);
  });

  describe('initialized()', function() {
    it('calls underlying initialized() only if not already inited', async () => {
      const core = MockCore();
      const wrapper = new CachingStoreWrapper(core, 0);

      var value = await promisifySingle(wrapper.initialized)();
      expect(value).toEqual(false);
      expect(core.initQueriedCount).toEqual(1);

      core.inited = true;

      value = await promisifySingle(wrapper.initialized)();
      expect(value).toEqual(true);
      expect(core.initQueriedCount).toEqual(2);

      core.inited = false; // this should have no effect since we already returned true

      value = await promisifySingle(wrapper.initialized)();
      expect(value).toEqual(true);
      expect(core.initQueriedCount).toEqual(2);
    });

    it('will not call initialized() if init() has been called', async () => {
      const core = MockCore();
      const wrapper = new CachingStoreWrapper(core, 0);

      var value = await promisifySingle(wrapper.initialized)();
      expect(value).toEqual(false);
      expect(core.initQueriedCount).toEqual(1);

      const allData = { features: {} };
      await promisifySingle(wrapper.init)(allData);

      value = await promisifySingle(wrapper.initialized)();
      expect(value).toEqual(true);
      expect(core.initQueriedCount).toEqual(1);
    });

    it('can cache false result', async () => {
      const core = MockCore();
      const wrapper = new CachingStoreWrapper(core, 1); // cache TTL = 1 second

      var value = await promisifySingle(wrapper.initialized)();
      expect(value).toEqual(false);
      expect(core.initQueriedCount).toEqual(1);

      core.inited = true;

      value = await promisifySingle(wrapper.initialized)();
      expect(value).toEqual(false);
      expect(core.initQueriedCount).toEqual(1);

      await sleepAsync(1100);
      
      value = await promisifySingle(wrapper.initialized)();
      expect(value).toEqual(true);
      expect(core.initQueriedCount).toEqual(2);
    });
  });

  describe('close()', function() {
    runCachedAndUncachedTests('closes underlying store', async (wrapper, core) => {
      wrapper.close();
      expect(core.closed).toBe(true);
    });
  });

  describe('core that uses initOrdered()', function() {
    runCachedAndUncachedTests('receives properly ordered data for init', async (wrapper, core) => {
      var dependencyOrderingTestData = {};
      dependencyOrderingTestData[features.namespace] = {
        a: { key: "a", prerequisites: [ { key: "b" }, { key: "c" } ] },
        b: { key: "b", prerequisites: [ { key: "c" }, { key: "e" } ] },
        c: { key: "c" },
        d: { key: "d" },
        e: { key: "e" },
        f: { key: "f" }
      };
      dependencyOrderingTestData[segments.namespace] = {
        o: { key: "o" }
      };
      await promisifySingle(wrapper.init)(dependencyOrderingTestData);
      
      var receivedData = core.data;
      expect(receivedData.length).toEqual(2);

      // Segments should always come first
      expect(receivedData[0].kind).toEqual(segments);
      expect(receivedData[0].items.length).toEqual(1);
      
      // Features should be ordered so that a flag always appears after its prerequisites, if any
      expect(receivedData[1].kind).toEqual(features);
      var featuresMap = dependencyOrderingTestData[features.namespace];
      var featuresList = receivedData[1].items;
      expect(featuresList.length).toEqual(Object.keys(featuresMap).length);
      for (var itemIndex in featuresList) {
        var item = featuresList[itemIndex];
        (item.prerequisites || []).forEach(function(prereq) {
          var prereqKey = prereq.key;
          var prereqItem = featuresMap[prereqKey];
          var prereqIndex = featuresList.indexOf(prereqItem);
          if (prereqIndex > itemIndex) {
            var allKeys = featuresList.map(f => f.key);
            throw new Error(item.key + " depends on " + prereqKey + ", but " + item.key +
              " was listed first; keys in order are [" + allKeys.join(", ") + "]");
          }
        });
      }
    }, MockOrderedCore);
  });
});
