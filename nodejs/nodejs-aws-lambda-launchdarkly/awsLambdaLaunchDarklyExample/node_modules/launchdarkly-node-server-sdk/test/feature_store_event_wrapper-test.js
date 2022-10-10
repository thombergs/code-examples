const EventEmitter = require('events').EventEmitter;
const FeatureStoreEventWrapper = require('../feature_store_event_wrapper');
const InMemoryFeatureStore = require('../feature_store');
const dataKind = require('../versioned_data_kind');
const { AsyncQueue, promisifySingle } = require('launchdarkly-js-test-helpers');

describe('FeatureStoreEventWrapper', () => {
  function listenAndStoreEvents(emitter, queue, eventName) {
    emitter.on(eventName, arg => {
      queue.add([eventName, arg]);
    });
  }

  it('sends events for init of empty store', async () => {
    const store = InMemoryFeatureStore();
    const allData = {
      features: {
        a: { key: 'a', version: 1 },
        b: { key: 'b', version: 1 }
      },
      segments: {}
    };
    const emitter = new EventEmitter();
    const queue = new AsyncQueue();
    listenAndStoreEvents(emitter, queue, 'update');
    listenAndStoreEvents(emitter, queue, 'update:a');
    listenAndStoreEvents(emitter, queue, 'update:b');

    const wrapper = FeatureStoreEventWrapper(store, emitter);

    await promisifySingle(wrapper.init)(allData);

    expect(await queue.take()).toEqual(['update', { key: 'a' }]);
    expect(await queue.take()).toEqual(['update:a', { key: 'a' }]);
    expect(await queue.take()).toEqual(['update', { key: 'b' }]);
    expect(await queue.take()).toEqual(['update:b', { key: 'b' }]);
    expect(queue.isEmpty()).toEqual(true);
  });

  it('sends events for reinit of non-empty store', async () => {
    const store = InMemoryFeatureStore();
    const allData0 = {
      features: {
        a: { key: 'a', version: 1 },
        b: { key: 'b', version: 1 },
        c: { key: 'c', version: 1 }
      },
      segments: {}
    };
    const allData1 = {
      features: {
        a: { key: 'a', version: 1 },
        b: { key: 'b', version: 2 }
      },
      segments: {}
    };
    const emitter = new EventEmitter();
    const queue = new AsyncQueue();
    listenAndStoreEvents(emitter, queue, 'update');
    listenAndStoreEvents(emitter, queue, 'update:a');
    listenAndStoreEvents(emitter, queue, 'update:b');
    listenAndStoreEvents(emitter, queue, 'update:c');

    const wrapper = FeatureStoreEventWrapper(store, emitter);

    await promisifySingle(wrapper.init)(allData0);

    expect(await queue.take()).toEqual(['update', { key: 'a' }]);
    expect(await queue.take()).toEqual(['update:a', { key: 'a' }]);
    expect(await queue.take()).toEqual(['update', { key: 'b' }]);
    expect(await queue.take()).toEqual(['update:b', { key: 'b' }]);
    expect(await queue.take()).toEqual(['update', { key: 'c' }]);
    expect(await queue.take()).toEqual(['update:c', { key: 'c' }]);
    expect(queue.isEmpty()).toEqual(true);
    
    await promisifySingle(wrapper.init)(allData1);
    expect(await queue.take()).toEqual(['update', { key: 'b' }]); // b was updated to version 2
    expect(await queue.take()).toEqual(['update:b', { key: 'b' }]);
    expect(await queue.take()).toEqual(['update', { key: 'c' }]); // c was deleted
    expect(await queue.take()).toEqual(['update:c', { key: 'c' }]);
    expect(queue.isEmpty()).toEqual(true);
  });

  it('sends event for update', async () => {
    const store = InMemoryFeatureStore();
    const allData = {
      features: {
        a: { key: 'a', version: 1 }
      },
      segments: {}
    };
    const emitter = new EventEmitter();
    const queue = new AsyncQueue();
    listenAndStoreEvents(emitter, queue, 'update');
    listenAndStoreEvents(emitter, queue, 'update:a');

    const wrapper = FeatureStoreEventWrapper(store, emitter);

    await promisifySingle(wrapper.init)(allData);

    expect(await queue.take()).toEqual(['update', { key: 'a' }]);
    expect(await queue.take()).toEqual(['update:a', { key: 'a' }]);
    expect(queue.isEmpty()).toEqual(true);

    await promisifySingle(wrapper.upsert)(dataKind.features, { key: 'a', version: 2 });
    await promisifySingle(wrapper.upsert)(dataKind.features, { key: 'a', version: 2 }); // no event for this one
    expect(await queue.take()).toEqual(['update', { key: 'a' }]);
    expect(await queue.take()).toEqual(['update:a', { key: 'a' }]);
    expect(queue.isEmpty()).toEqual(true);
  });

  it('sends event for delete', async () => {
    const store = InMemoryFeatureStore();
    const allData = {
      features: {
        a: { key: 'a', version: 1 }
      },
      segments: {}
    };
    const emitter = new EventEmitter();
    const queue = new AsyncQueue();
    listenAndStoreEvents(emitter, queue, 'update');
    listenAndStoreEvents(emitter, queue, 'update:a');

    const wrapper = FeatureStoreEventWrapper(store, emitter);

    await promisifySingle(wrapper.init)(allData);

    expect(await queue.take()).toEqual(['update', { key: 'a' }]);
    expect(await queue.take()).toEqual(['update:a', { key: 'a' }]);
    expect(queue.isEmpty()).toEqual(true);

    await promisifySingle(wrapper.delete)(dataKind.features, 'a', 2);
    expect(await queue.take()).toEqual(['update', { key: 'a' }]);
    expect(await queue.take()).toEqual(['update:a', { key: 'a' }]);
    expect(queue.isEmpty()).toEqual(true);
  });

  it('sends update events for transitive dependencies', async () => {
    const store = InMemoryFeatureStore();
    const allData = {
      features: {
        a: { key: 'a', version: 1 },
        b: { key: 'b', version: 1, prerequisites: [ { key: 'c' }, { key: 'e' } ] },
        c: { key: 'c', version: 1, prerequisites: [ { key: 'd' } ],
          rules: [
            { clauses: [ { op: 'segmentMatch', values: [ 's0' ] } ] }
          ]
        },
        d: { key: 'd', version: 1, prerequisites: [ { key: 'e' } ] },
        e: { key: 'e', version: 1 }
      },
      segments: {
        s0: { key: 's0', version: 1 }
      }
    };
    const emitter = new EventEmitter();
    const queue = new AsyncQueue();
    listenAndStoreEvents(emitter, queue, 'update');

    const wrapper = FeatureStoreEventWrapper(store, emitter);

    await promisifySingle(wrapper.init)(allData);

    expect(await queue.take()).toEqual(['update', { key: 'a' }]);
    expect(await queue.take()).toEqual(['update', { key: 'b' }]);
    expect(await queue.take()).toEqual(['update', { key: 'c' }]);
    expect(await queue.take()).toEqual(['update', { key: 'd' }]);
    expect(await queue.take()).toEqual(['update', { key: 'e' }]);
    expect(queue.isEmpty()).toEqual(true);

    await promisifySingle(wrapper.upsert)(dataKind.features,
      { key: 'd', version: 2, prerequisites: [ { key: 'e' } ] });
    expect(await queue.take()).toEqual(['update', { key: 'b' }]);
    expect(await queue.take()).toEqual(['update', { key: 'c' }]);
    expect(await queue.take()).toEqual(['update', { key: 'd' }]);

    await promisifySingle(wrapper.upsert)(dataKind.segments, { key: 's0', version: 2 });
    expect(await queue.take()).toEqual(['update', { key: 'b' }]);
    expect(await queue.take()).toEqual(['update', { key: 'c' }]);
  });
});
