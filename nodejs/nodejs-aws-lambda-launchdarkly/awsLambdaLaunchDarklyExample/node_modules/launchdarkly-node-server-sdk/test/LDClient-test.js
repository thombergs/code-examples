var LDClient = require('../index.js');
var messages = require('../messages');
var stubs = require('./stubs');

describe('LDClient', () => {

  describe('ready event', () => {
    it('is fired in offline mode', done => {
      var client = LDClient.init('sdk_key', { offline: true });
      client.on('ready', () => {
        done();
      });
    });
  });

  describe('failed event', () => {
    it('is fired if initialization fails', done => {
      var updateProcessor = stubs.stubUpdateProcessor();
      updateProcessor.error = { status: 403 };
      var client = stubs.createClient({ updateProcessor: updateProcessor }, {});

      client.on('failed', err => {
        expect(err).toEqual(updateProcessor.error);
        done();
      });
    });
  });

  describe('isOffline()', () => {
    it('returns true in offline mode', done => {
      var client = LDClient.init('sdk_key', {offline: true});
      client.on('ready', () => {
        expect(client.isOffline()).toEqual(true);
        done();
      });
    });
  });

  describe('secureModeHash()', () => {
    it('correctly computes hash for a known message and secret', () => {
      var client = LDClient.init('secret', {offline: true});
      var hash = client.secureModeHash({"key": "Message"});
      expect(hash).toEqual("aa747c502a898200f9e4fa21bac68136f886a0e27aec70ba06daf2e2a5cb5597");
    });
  });

  describe('waitForInitialization()', () => {
    it('resolves when ready', async () => {
      var client = stubs.createClient({}, {});
      await client.waitForInitialization();
    });

    it('resolves immediately if the client is already ready', async () => {
      var client = stubs.createClient({}, {});
      await client.waitForInitialization();
      await client.waitForInitialization();
    });

    it('is rejected if initialization fails', async () => {
      var err = { status: 403 };
      var updateProcessor = stubs.stubUpdateProcessor();
      updateProcessor.error = err;
      var client = stubs.createClient({ updateProcessor: updateProcessor }, {});
      await expect(client.waitForInitialization()).rejects.toBe(err);
    });

    it('creates only one Promise', async () => {
      const updateProcessor = stubs.stubUpdateProcessor();
      updateProcessor.shouldInitialize = false;
      const client = stubs.createClient({ updateProcessor: updateProcessor }, {});
      const p1 = client.waitForInitialization();
      const p2 = client.waitForInitialization();
      expect(p2).toBe(p1);
    })
  });

  describe('close()', () => {
    it('does not crash when closing an offline client', done => {
      var client = LDClient.init('sdk_key', {offline: true});
      expect(() => client.close()).not.toThrow();
      done();
    });
  });
});
