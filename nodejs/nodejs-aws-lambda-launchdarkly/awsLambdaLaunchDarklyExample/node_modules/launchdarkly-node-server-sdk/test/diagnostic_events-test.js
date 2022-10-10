const os = require('os');
const packageJson = require('../package.json');
const configuration = require('../configuration');
const { DiagnosticsManager, DiagnosticId } = require('../diagnostic_events');

describe('DiagnosticId', () => {
  it('uses last 6 characters of SDK key', () => {
    const id = DiagnosticId('my-sdk-key');
    expect(id.sdkKeySuffix).toEqual('dk-key');
  });

  it('creates random UUID', () => {
    const id0 = DiagnosticId('my-sdk-key');
    const id1 = DiagnosticId('my-sdk-key');
    expect(id0.diagnosticId).toBeTruthy();
    expect(id1.diagnosticId).toBeTruthy();
    expect(id0.diagnosticId).not.toEqual(id1.diagnosticId);
  });
});

describe('DiagnosticsManager', () => {
  const id = DiagnosticId('my-sdk-key');
  const defaultConfig = configuration.validate({});
 
  it('copies DiagnosticId', () => {
    const manager = DiagnosticsManager(defaultConfig, id, 100000);
    const event = manager.createInitEvent();
    expect(event.id).toEqual(id);
  });

  it('copies start time', () => {
    const manager = DiagnosticsManager(defaultConfig, id, 100000);
    const event = manager.createInitEvent();
    expect(event.creationDate).toEqual(100000);
  });

  it('provides SDK data', () => {
    const manager = DiagnosticsManager(defaultConfig, id, 100000);
    const event = manager.createInitEvent();
    expect(event.sdk).toEqual({
      name: 'node-server-sdk',
      version: packageJson.version
    });
  });

  it('provides platform data', () => {
    const manager = DiagnosticsManager(defaultConfig, id, 100000);
    const event = manager.createInitEvent();
    expect(event.platform).toEqual({
      name: 'Node',
      osArch: os.arch(),
      osName: event.platform.osName, // this may have been transformed by normalizePlatformName
      osVersion: os.release(),
      nodeVersion: process.versions.node,
    });
  });

  function verifyConfig(configIn, configOut) {
    const config = configuration.validate(configIn);
    const manager = DiagnosticsManager(config, id, 100000);
    const event = manager.createInitEvent();
    expect(event.configuration).toMatchObject(configOut);
  }

  it('translates default configuration', () => {
    verifyConfig({}, {
      allAttributesPrivate: false,
      connectTimeoutMillis: 5000,
      customBaseURI: false,
      customEventsURI: false,
      customStreamURI: false,
      dataStoreType: 'memory',
      diagnosticRecordingIntervalMillis: 900000,
      eventsCapacity: 10000,
      eventsFlushIntervalMillis: 5000,
      inlineUsersInEvents: false,
      offline: false,
      pollingIntervalMillis: 30000,
      reconnectTimeMillis: 1000,
      socketTimeoutMillis: 5000,
      streamingDisabled: false,
      userKeysCapacity: 1000,
      userKeysFlushIntervalMillis: 300000,
      usingProxy: false,
      usingProxyAuthenticator: false,
      usingRelayDaemon: false,
    });
  });

  it('translates custom configuration', () => {
    verifyConfig({ baseUri: 'http://other' }, {
      customBaseURI: true,
      customEventsURI: false,
      customStreamURI: false,
    });
    verifyConfig({ eventsUri: 'http://other' }, {
      customBaseURI: false,
      customEventsURI: true,
      customStreamURI: false,
    });
    verifyConfig({ streamUri: 'http://other' }, {
      customBaseURI: false,
      customEventsURI: false,
      customStreamURI: true,
    });
    verifyConfig({ allAttributesPrivate: true }, { allAttributesPrivate: true });
    verifyConfig({ timeout: 6 }, { connectTimeoutMillis: 6000, socketTimeoutMillis: 6000 });
    verifyConfig({ diagnosticRecordingInterval: 999 }, { diagnosticRecordingIntervalMillis: 999000 });
    verifyConfig({ capacity: 999 }, { eventsCapacity: 999 });
    verifyConfig({ flushInterval: 33 }, { eventsFlushIntervalMillis: 33000 });
    verifyConfig({ stream: false }, { streamingDisabled: true });
    verifyConfig({ streamInitialReconnectDelay: 33 }, { reconnectTimeMillis: 33000 });
    verifyConfig({ userKeysCapacity: 111 }, { userKeysCapacity: 111 });
    verifyConfig({ userKeysFlushInterval: 33 }, { userKeysFlushIntervalMillis: 33000 });
    verifyConfig({ useLdd: true }, { usingRelayDaemon: true });

    const fakeProxy = {};
    verifyConfig({ proxyAgent: fakeProxy }, { usingProxy: true, usingProxyAuthenticator: false });
    verifyConfig({ proxyHost: 'my-proxy' }, { usingProxy: true, usingProxyAuthenticator: false });
    verifyConfig({ proxyAgent: fakeProxy, proxyAuth: 'basic' }, { usingProxy: true, usingProxyAuthenticator: true });

    const fakeStore = { description: 'WeirdStore' };
    verifyConfig({ featureStore: fakeStore }, { dataStoreType: fakeStore.description });
  });

  it('creates periodic event from stats, then resets', () => {
    const manager = DiagnosticsManager(defaultConfig, id, 100000);
    const timeBeforeReset = new Date().getTime();
    const event1 = manager.createStatsEventAndReset(4, 5, 6);

    expect(event1).toMatchObject({
      kind: 'diagnostic',
      dataSinceDate: 100000,
      droppedEvents: 4,
      deduplicatedUsers: 5,
      eventsInLastBatch: 6,
    });

    expect(event1.creationDate).toBeGreaterThanOrEqual(timeBeforeReset);

    const event2 = manager.createStatsEventAndReset(1, 2, 3);

    expect(event2).toMatchObject({
      kind: 'diagnostic',
      dataSinceDate: event1.creationDate,
      droppedEvents: 1,
      deduplicatedUsers: 2,
      eventsInLastBatch: 3,
    });

    expect(event2.creationDate).toBeGreaterThanOrEqual(event1.creationDate);
  });
});
