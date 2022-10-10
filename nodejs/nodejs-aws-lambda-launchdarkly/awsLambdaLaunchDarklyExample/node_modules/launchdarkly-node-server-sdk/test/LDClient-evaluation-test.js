const { TestData } = require('../integrations');
const stubs = require('./stubs');

describe('LDClient', () => {

  var defaultUser = { key: 'user' };

  describe('variation()', () => {
    it('evaluates an existing flag', async () => {
      const td = TestData();
      td.update(td.flag('flagkey').on(true).variations('a', 'b').fallthroughVariation(1));
      var client = stubs.createClient({ updateProcessor: td });
      await client.waitForInitialization();
      var result = await client.variation('flagkey', defaultUser, 'c');
      expect(result).toEqual('b');
    });

    it('returns default for unknown flag', async () => {
      var client = stubs.createClient({}, {});
      await client.waitForInitialization();
      var result = await client.variation('flagkey', defaultUser, 'default');
      expect(result).toEqual('default');
    });

    it('returns default if client is offline', async () => {
      const td = TestData();
      td.update(td.flag('flagkey').variations('value').variationForAllUsers(0));
      var logger = stubs.stubLogger();
      var client = stubs.createClient({ offline: true, updateProcessor: td, logger });
      await client.waitForInitialization();
      var result = await client.variation('flagkey', defaultUser, 'default');
      expect(result).toEqual('default');
      expect(logger.info).toHaveBeenCalled();
    });

    it('returns default if client and store are not initialized', async () => {
      // Can't use TestData to set up this condition, because it always initializes successfully
      const flag = {
        key: 'flagkey',
        version: 1,
        on: false,
        offVariation: 0,
        variations: ['value']
      };
      const featureStore = stubs.uninitializedStoreWithFlags(flag);
      var client = stubs.createClient({ featureStore });
      var result = await client.variation('flagkey', defaultUser, 'default');
      expect(result).toEqual('default');
    });

    it('returns value from store if store is initialized but client is not', async () => {
      // Can't use TestData to set up this condition, because it always initializes successfully
      var featureStore = stubs.initializedStoreWithFlags({
        key: 'flagkey',
        version: 1,
        on: false,
        offVariation: 0,
        variations: ['value']
      });
      var logger = stubs.stubLogger();
      var updateProcessor = stubs.stubUpdateProcessor();
      updateProcessor.shouldInitialize = false;
      var client = stubs.createClient({ updateProcessor, featureStore, logger });
      var result = await client.variation('flagkey', defaultUser, 'default');
      expect(result).toEqual('value');
      expect(logger.warn).toHaveBeenCalled();
    });

    it('returns default if flag key is not specified', async () => {
      var client = stubs.createClient({}, {});
      await client.waitForInitialization();
      var result = await client.variation(null, defaultUser, 'default');
      expect(result).toEqual('default');
    });

    it('returns default for flag that evaluates to null', async () => {
      const td = TestData();
      td.usePreconfiguredFlag({ // TestData normally won't construct a flag with offVariation: null
        key: 'flagkey',
        on: false,
        offVariation: null
      });
      var client = stubs.createClient({ updateProcessor: td });
      await client.waitForInitialization();
      var result = await client.variation('flagkey', defaultUser, 'default');
      expect(result).toEqual('default');
    });

    it('can use a callback instead of a Promise', done => {
      var client = stubs.createClient({}, {});
      client.on('ready', () => {
        client.variation('flagkey', defaultUser, 'default', (err, result) => {
          expect(err).toBeNull();
          expect(result).toEqual('default');
          done();
        });
      });
    });
  });

  describe('variationDetail()', () => {
    it('evaluates an existing flag', async () => {
      const td = TestData();
      td.update(td.flag('flagkey').on(true).variations('a', 'b').fallthroughVariation(1));
      var client = stubs.createClient({ updateProcessor: td });
      await client.waitForInitialization();
      var result = await client.variationDetail('flagkey', defaultUser, 'c');
      expect(result).toMatchObject({ value: 'b', variationIndex: 1, reason: { kind: 'FALLTHROUGH' } });
    });

    it('returns default for unknown flag', async () => {
      var client = stubs.createClient({}, { });
      await client.waitForInitialization();
      var result = await client.variationDetail('flagkey', defaultUser, 'default');
      expect(result).toMatchObject({ value: 'default', variationIndex: null,
        reason: { kind: 'ERROR', errorKind: 'FLAG_NOT_FOUND' } });
    });

    it('returns default if client is offline', async () => {
      // Can't use TestData to set up this condition, because the data source isn't used in offline mode
      var flag = {
        key: 'flagkey',
        version: 1,
        on: false,
        offVariation: 0,
        variations: ['value']
      };
      var logger = stubs.stubLogger();
      var client = stubs.createClient({ offline: true, logger: logger }, { flagkey: flag });
      await client.waitForInitialization();
      var result = await client.variationDetail('flagkey', defaultUser, 'default');
      expect(result).toMatchObject({ value: 'default', variationIndex: null,
        reason: { kind: 'ERROR', errorKind: 'CLIENT_NOT_READY' }});
      expect(logger.info).toHaveBeenCalled();
    });

    it('returns default if client and store are not initialized', async () => {
      // Can't use TestData to set up this condition, because it always initializes successfully
      var flag = {
        key: 'flagkey',
        version: 1,
        on: false,
        offVariation: 0,
        variations: ['value']
      };
      const featureStore = stubs.uninitializedStoreWithFlags(flag);
      var client = stubs.createClient({ featureStore });
      var result = await client.variationDetail('flagkey', defaultUser, 'default');
      expect(result).toMatchObject({ value: 'default', variationIndex: null,
        reason: { kind: 'ERROR', errorKind: 'CLIENT_NOT_READY' } });
    });

    it('returns value from store if store is initialized but client is not', async () => {
      // Can't use TestData to set up this condition, because it always initializes successfully
      var featureStore = stubs.initializedStoreWithFlags({
        key: 'flagkey',
        version: 1,
        on: false,
        offVariation: 0,
        variations: ['value']
      });
      var logger = stubs.stubLogger();
      var updateProcessor = stubs.stubUpdateProcessor();
      updateProcessor.shouldInitialize = false;
      var client = stubs.createClient({ updateProcessor, featureStore, logger });
      var result = await client.variationDetail('flagkey', defaultUser, 'default');
      expect(result).toMatchObject({ value: 'value', variationIndex: 0, reason: { kind: 'OFF' }})
      expect(logger.warn).toHaveBeenCalled();
    });

    it('returns default if flag key is not specified', async () => {
      var client = stubs.createClient({}, { });
      await client.waitForInitialization();
      var result = await client.variationDetail(null, defaultUser, 'default');
      expect(result).toMatchObject({ value: 'default', variationIndex: null,
        reason: { kind: 'ERROR', errorKind: 'FLAG_NOT_FOUND' } });
    });

    it('returns default for flag that evaluates to null', async () => {
      const td = TestData();
      td.usePreconfiguredFlag({ // TestData normally won't construct a flag with offVariation: null
        key: 'flagkey',
        on: false,
        offVariation: null
      });
      var client = stubs.createClient({ updateProcessor: td });
      await client.waitForInitialization();
      var result = await client.variationDetail('flagkey', defaultUser, 'default');
      expect(result).toMatchObject({ value: 'default', variationIndex: null, reason: { kind: 'OFF' } });
    });

    it('can use a callback instead of a Promise', done => {
      var client = stubs.createClient({}, {});
      client.on('ready', () => {
        client.variationDetail('flagkey', defaultUser, 'default', (err, result) => {
          expect(err).toBeNull();
          expect(result).toMatchObject({ value: 'default', variationIndex: null,
            reason: { kind: 'ERROR', errorKind: 'FLAG_NOT_FOUND' } });
          done();
        });
      });
    });
  });
});
