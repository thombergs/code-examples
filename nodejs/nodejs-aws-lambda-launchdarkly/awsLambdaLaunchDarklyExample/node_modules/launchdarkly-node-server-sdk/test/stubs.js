const { withCloseable } = require('launchdarkly-js-test-helpers');
var InMemoryFeatureStore = require('../feature_store');
var LDClient = require('../index.js');
var dataKind = require('../versioned_data_kind');

import { AsyncQueue } from 'launchdarkly-js-test-helpers';

import { format } from 'util';

function stubEventProcessor() {
  var eventProcessor = {
    events: [],
    sendEvent: function(event) {
      eventProcessor.events.push(event);
    },
    flush: function(callback) {
      if (callback) {
        setImmediate(callback);
      } else {
        return Promise.resolve(null);
      }
    },
    close: function() {}
  };
  return eventProcessor;
}

function stubLogger() {
  return {
    debug: jest.fn(),
    info: jest.fn(),
    warn: jest.fn(),
    error: jest.fn()
  };
}

function asyncLogCapture() {
  const logCapture = {all: new AsyncQueue()};
  const logger = {};
  for (const level of ['debug', 'info', 'warn', 'error']) {
    logCapture[level] = new AsyncQueue();
    logger[level] = function(fmt, ...args) {
      const message = format(fmt, ...args);
      logCapture[level].add(message);
      logCapture.all.add({ level: level, message: message });
    }
  }
  logCapture.logger = logger;
  return logCapture;
}

function stubUpdateProcessor() {
  var updateProcessor = {
    start: function(callback) {
      if (updateProcessor.shouldInitialize) {
        setImmediate(callback, updateProcessor.error);
      }
    },
    shouldInitialize: true
  };
  return updateProcessor;
}

function createClient(overrideOptions) {
  var defaults = {
    eventProcessor: stubEventProcessor(),
    updateProcessor: stubUpdateProcessor(),
    logger: stubLogger()
  };
  return LDClient.init('secret', Object.assign({}, defaults, overrideOptions));
}

async function withClient(overrideOptions, callback) {
  return withCloseable(createClient(overrideOptions), callback);
}

function initializedStoreWithFlags(...flags) {
  const flagsMap = {};
  for (const f of flags) {
    flagsMap[f.key] = f;
  }
  const store = InMemoryFeatureStore();
  store.init({
    [dataKind.features.namespace]: flagsMap,
    [dataKind.segments.namespace]: {}
  });
  return store;
}

function uninitializedStoreWithFlags(...flags) {
  const store = InMemoryFeatureStore();
  for (const f of flags) {
    store.upsert(dataKind.features, f);
  }
  return store;
}

module.exports = {
  asyncLogCapture,
  createClient,
  initializedStoreWithFlags,
  stubEventProcessor,
  stubLogger,
  stubUpdateProcessor,
  uninitializedStoreWithFlags,
  withClient,
};
