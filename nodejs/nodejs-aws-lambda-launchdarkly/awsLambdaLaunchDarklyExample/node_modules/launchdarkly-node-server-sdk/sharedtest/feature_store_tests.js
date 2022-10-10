const dataKind = require('../versioned_data_kind');

const { promisifySingle, withCloseable, AsyncQueue } = require('launchdarkly-js-test-helpers');

function runFeatureStoreTests(createStore, clearExistingData) {
  const feature1 = { key: 'foo', version: 10 };
  const feature2 = { key: 'bar', version: 10 };

  async function withInitedStore(asyncAction) {
    if (clearExistingData) {
      await clearExistingData();
    }
    const store = createStore();
    await withCloseable(store, async () => {
      const initData = {
        [dataKind.features.namespace]: {
          foo: feature1,
          bar: feature2,
        },
        [dataKind.segments.namespace]: {},
      };
      await promisifySingle(store.init)(initData);
      await asyncAction(store);
    });
  }

  it('is initialized after calling init()', async () => {
    await withInitedStore(async store => {
      const result = await promisifySingle(store.initialized)();
      expect(result).toBe(true);
    });
  });

  it('init() completely replaces previous data', async () => {
    await withInitedStore(async store => {
      const flags = {
        first: { key: 'first', version: 1 },
        second: { key: 'second', version: 1 },
      };
      const segments = { first: { key: 'first', version: 2 } };
      const initData1 = {
        [dataKind.features.namespace]: flags,
        [dataKind.segments.namespace]: segments,
      };

      await promisifySingle(store.init)(initData1);
      const items1 = await promisifySingle(store.all)(dataKind.features);
      expect(items1).toEqual(flags);
      const items2 = await promisifySingle(store.all)(dataKind.segments);
      expect(items2).toEqual(segments);

      const newFlags = { first: { key: 'first', version: 3 } };
      const newSegments = { first: { key: 'first', version: 4 } };
      const initData2 = {
        [dataKind.features.namespace]: newFlags,
        [dataKind.segments.namespace]: newSegments,
      };

      await promisifySingle(store.init)(initData2);
      const items3 = await promisifySingle(store.all)(dataKind.features);
      expect(items3).toEqual(newFlags);
      const items4 = await promisifySingle(store.all)(dataKind.segments);
      expect(items4).toEqual(newSegments);
    });
  });

  it('gets existing feature', async () => {
    await withInitedStore(async store => {
      const result = await promisifySingle(store.get)(dataKind.features, feature1.key);
      expect(result).toEqual(feature1);
    });
  });

  it('does not get nonexisting feature', async () => {
    await withInitedStore(async store => {
      const result = await promisifySingle(store.get)(dataKind.features, 'biz');
      expect(result).toBe(null);
    });
  });

  it('gets all features', async () => {
    await withInitedStore(async store => {
      const result = await promisifySingle(store.all)(dataKind.features);
      expect(result).toEqual({
        foo: feature1,
        bar: feature2,
      });
    });
  });

  it('upserts with newer version', async () => {
    const newVer = { key: feature1.key, version: feature1.version + 1 };
    await withInitedStore(async store => {
      await promisifySingle(store.upsert)(dataKind.features, newVer);
      const result = await promisifySingle(store.get)(dataKind.features, feature1.key);
      expect(result).toEqual(newVer);
    });
  });

  it('does not upsert with older version', async () => {
    const oldVer = { key: feature1.key, version: feature1.version - 1 };
    await withInitedStore(async store => {
      await promisifySingle(store.upsert)(dataKind.features, oldVer);
      const result = await promisifySingle(store.get)(dataKind.features, feature1.key);
      expect(result).toEqual(feature1);
    });
  });

  it('upserts new feature', async () => {
    const newFeature = { key: 'biz', version: 99 };
    await withInitedStore(async store => {
      await promisifySingle(store.upsert)(dataKind.features, newFeature);
      const result = await promisifySingle(store.get)(dataKind.features, newFeature.key);
      expect(result).toEqual(newFeature);
    });
  });

  it('handles upsert race condition within same client correctly', async () => {
    // Not sure if there is a way to do this one with async/await
    const ver1 = { key: feature1.key, version: feature1.version + 1 };
    const ver2 = { key: feature1.key, version: feature1.version + 2 };
    const calls = new AsyncQueue();
    await withInitedStore(async store => {
      const callback = () => {
        calls.add(null);
      };

      // Deliberately do not wait for the first upsert to complete before starting the second,
      // so their transactions will be interleaved unless we're correctly serializing updates
      store.upsert(dataKind.features, ver2, callback);
      store.upsert(dataKind.features, ver1, callback);

      // Now wait until both have completed
      await calls.take();
      await calls.take();
      const result = await promisifySingle(store.get)(dataKind.features, feature1.key);
      expect(result).toEqual(ver2);
    });
  });

  it('deletes with newer version', async () => {
    await withInitedStore(async store => {
      await promisifySingle(store.delete)(dataKind.features, feature1.key, feature1.version + 1);
      const result = await promisifySingle(store.get)(dataKind.features, feature1.key);
      expect(result).toBe(null);
    });
  });

  it('does not delete with older version', async () => {
    await withInitedStore(async store => {
      await promisifySingle(store.delete)(dataKind.features, feature1.key, feature1.version - 1);
      const result = await promisifySingle(store.get)(dataKind.features, feature1.key);
      expect(result).not.toBe(null);
    });
  });

  it('allows deleting unknown feature', async () => {
    await withInitedStore(async store => {
      await promisifySingle(store.delete)(dataKind.features, 'biz', 99);
      const result = await promisifySingle(store.get)(dataKind.features, 'biz');
      expect(result).toBe(null);
    });
  });

  it('does not upsert older version after delete', async () => {
    await withInitedStore(async store => {
      await promisifySingle(store.delete)(dataKind.features, feature1.key, feature1.version + 1);
      await promisifySingle(store.upsert)(dataKind.features, feature1);
      const result = await promisifySingle(store.get)(dataKind.features, feature1.key);
      expect(result).toBe(null);
    });
  });
}

module.exports = {
  runFeatureStoreTests,
};
