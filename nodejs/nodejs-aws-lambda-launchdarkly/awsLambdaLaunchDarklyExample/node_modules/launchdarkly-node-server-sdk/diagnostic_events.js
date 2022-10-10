const os = require('os');
const { v4: uuidv4 } = require('uuid');
const configuration = require('./configuration');
const packageJson = require('./package.json');

// An object that maintains information that will go into diagnostic events, and knows how to format
// those events. It is instantiated by the SDK client, and shared with the event processor.
function DiagnosticsManager(config, diagnosticId, startTime) {
  let dataSinceDate = startTime;
  let streamInits = [];
  const acc = {};

  // Creates the initial event that is sent by the event processor when the SDK starts up. This will not
  // be repeated during the lifetime of the SDK client.
  acc.createInitEvent = () => ({
    kind: 'diagnostic-init',
    id: diagnosticId,
    creationDate: startTime,
    sdk: makeSdkData(config),
    configuration: makeConfigData(config),
    platform: makePlatformData(),
  });

  // Records a stream connection attempt (called by the stream processor).
  // timestamp: Time of the *beginning* of the connection attempt.
  // failed: True if the connection failed, or we got a read timeout before receiving a "put".
  // durationMillis: Elapsed time between starting timestamp and when we either gave up/lost the
  //   connection or received a successful "put".
  acc.recordStreamInit = (timestamp, failed, durationMillis) => {
    const item = { timestamp, failed, durationMillis };
    streamInits.push(item);
  };

  // Creates a periodic event containing time-dependent stats, and resets the state of the manager with
  // regard to those stats.
  // Note: the reason droppedEvents, deduplicatedUsers, and eventsInLastBatch are passed into this function,
  // instead of being properties of the DiagnosticsManager, is that the event processor is the one who's
  // calling this function and is also the one who's tracking those stats.
  acc.createStatsEventAndReset = (droppedEvents, deduplicatedUsers, eventsInLastBatch) => {
    const currentTime = new Date().getTime();
    const ret = {
      kind: 'diagnostic',
      id: diagnosticId,
      creationDate: currentTime,
      dataSinceDate,
      droppedEvents,
      deduplicatedUsers,
      eventsInLastBatch,
      streamInits,
    };
    dataSinceDate = currentTime;
    streamInits = [];
    return ret;
  };

  return acc;
}

function DiagnosticId(sdkKey) {
  const ret = {
    diagnosticId: uuidv4(),
  };
  if (sdkKey) {
    ret.sdkKeySuffix = sdkKey.length > 6 ? sdkKey.substring(sdkKey.length - 6) : sdkKey;
  }
  return ret;
}

function makeSdkData(config) {
  const sdkData = {
    name: 'node-server-sdk',
    version: packageJson.version,
  };
  if (config.wrapperName) {
    sdkData.wrapperName = config.wrapperName;
  }
  if (config.wrapperVersion) {
    sdkData.wrapperVersion = config.wrapperVersion;
  }
  return sdkData;
}

function makeConfigData(config) {
  const defaults = configuration.defaults();
  const secondsToMillis = sec => Math.trunc(sec * 1000);

  function getComponentDescription(component, defaultName) {
    if (component) {
      return component.description || 'custom';
    }
    return defaultName;
  }

  const configData = {
    customBaseURI: config.baseUri !== defaults.baseUri,
    customStreamURI: config.streamUri !== defaults.streamUri,
    customEventsURI: config.eventsUri !== defaults.eventsUri,
    eventsCapacity: config.capacity,
    connectTimeoutMillis: secondsToMillis(config.timeout),
    socketTimeoutMillis: secondsToMillis(config.timeout), // Node doesn't distinguish between these two kinds of timeouts
    eventsFlushIntervalMillis: secondsToMillis(config.flushInterval),
    pollingIntervalMillis: secondsToMillis(config.pollInterval),
    // startWaitMillis: n/a (Node SDK does not have this feature)
    // samplingInterval: n/a (Node SDK does not have this feature)
    reconnectTimeMillis: secondsToMillis(config.streamInitialReconnectDelay),
    streamingDisabled: !config.stream,
    usingRelayDaemon: !!config.useLdd,
    offline: !!config.offline,
    allAttributesPrivate: !!config.allAttributesPrivate,
    inlineUsersInEvents: !!config.inlineUsersInEvents,
    userKeysCapacity: config.userKeysCapacity,
    userKeysFlushIntervalMillis: secondsToMillis(config.userKeysFlushInterval),
    usingProxy: !!(config.proxyAgent || config.proxyHost),
    usingProxyAuthenticator: !!config.proxyAuth,
    diagnosticRecordingIntervalMillis: secondsToMillis(config.diagnosticRecordingInterval),
    dataStoreType: getComponentDescription(config.featureStore, 'memory'),
  };

  return configData;
}

function makePlatformData() {
  return {
    name: 'Node',
    osArch: os.arch(),
    osName: normalizePlatformName(os.platform()),
    osVersion: os.release(),
    // Note that os.release() is not the same OS version string that would be reported by other languages.
    // It's defined as being the value returned by "uname -r" (e.g. on Mac OS 10.14, this is "18.7.0"; on
    // Ubuntu 16.04, it is "4.4.0-1095-aws"), or GetVersionExW in Windows.
    nodeVersion: process.versions.node,
  };
}

function normalizePlatformName(platformName) {
  // The following logic is based on how Node.js reports the platform name
  switch (platformName) {
    case 'darwin':
      return 'MacOS';
    case 'win32':
      return 'Windows';
    case 'linux':
      return 'Linux';
    default:
      return platformName;
  }
}

module.exports = {
  DiagnosticsManager,
  DiagnosticId,
};
