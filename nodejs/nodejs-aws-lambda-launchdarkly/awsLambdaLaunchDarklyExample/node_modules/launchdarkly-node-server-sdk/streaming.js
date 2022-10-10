const errors = require('./errors');
const httpUtils = require('./utils/httpUtils');
const messages = require('./messages');
const { EventSource } = require('launchdarkly-eventsource');
const dataKind = require('./versioned_data_kind');

// The read timeout for the stream is a fixed value that is set to be slightly longer than the expected
// interval between heartbeats from the LaunchDarkly streaming server. If this amount of time elapses
// with no new data, the connection will be cycled.
const streamReadTimeoutMillis = 5 * 60 * 1000; // 5 minutes

// Note that the requestor parameter is unused now that LD no longer uses "indirect" stream
// events. The parameter is retained here for backward compatibility with any code that uses
// this constructor directly, since it is documented in index.d.ts.
function StreamProcessor(sdkKey, config, requestor, diagnosticsManager, specifiedEventSourceFactory) {
  const processor = {},
    featureStore = config.featureStore;
  let es;
  let connectionAttemptStartTime;

  const headers = httpUtils.getDefaultHeaders(sdkKey, config);

  const eventSourceFactory = specifiedEventSourceFactory || EventSource;

  function getKeyFromPath(kind, path) {
    return path.startsWith(kind.streamApiPath) ? path.substring(kind.streamApiPath.length) : null;
  }

  function logConnectionStarted() {
    connectionAttemptStartTime = new Date().getTime();
  }

  function logConnectionResult(success) {
    if (connectionAttemptStartTime && diagnosticsManager) {
      diagnosticsManager.recordStreamInit(
        connectionAttemptStartTime,
        !success,
        new Date().getTime() - connectionAttemptStartTime
      );
    }
    connectionAttemptStartTime = null;
  }

  processor.start = fn => {
    const cb = fn || function () {};

    logConnectionStarted();

    function handleError(err) {
      // launchdarkly-eventsource expects this function to return true if it should retry, false to shut down.
      if (err.status && !errors.isHttpErrorRecoverable(err.status)) {
        const message = messages.httpErrorMessage(err, 'streaming request');
        config.logger.error(message);
        logConnectionResult(false);
        cb(new errors.LDStreamingError(err.message, err.status));
        return false;
      }
      const message = messages.httpErrorMessage(err, 'streaming request', 'will retry');
      config.logger.warn(message);
      logConnectionResult(false);
      logConnectionStarted();
      return true;
    }

    es = new eventSourceFactory(config.streamUri + '/all', {
      agent: config.proxyAgent,
      errorFilter: handleError,
      headers,
      initialRetryDelayMillis: 1000 * config.streamInitialReconnectDelay,
      readTimeoutMillis: streamReadTimeoutMillis,
      retryResetIntervalMillis: 60000,
      tlsParams: config.tlsParams,
    });

    es.onclose = () => {
      config.logger.info('Closed LaunchDarkly stream connection');
    };

    // This stub handler only exists because error events must have a listener; handleError() does the work.
    es.onerror = () => {};

    es.onopen = () => {
      config.logger.info('Opened LaunchDarkly stream connection');
    };

    es.onretrying = e => {
      config.logger.info('Will retry stream connection in ' + e.delayMillis + ' milliseconds');
    };

    function reportJsonError(type, data) {
      config.logger.error('Stream received invalid data in "' + type + '" message');
      config.logger.debug('Invalid JSON follows: ' + data);
      cb(new errors.LDStreamingError('Malformed JSON data in event stream'));
    }

    es.addEventListener('put', e => {
      config.logger.debug('Received put event');
      if (e && e.data) {
        logConnectionResult(true);
        let all;
        try {
          all = JSON.parse(e.data);
        } catch (err) {
          reportJsonError('put', e.data);
          return;
        }
        const initData = {};
        initData[dataKind.features.namespace] = all.data.flags;
        initData[dataKind.segments.namespace] = all.data.segments;
        featureStore.init(initData, () => {
          cb();
        });
      } else {
        cb(new errors.LDStreamingError('Unexpected payload from event stream'));
      }
    });

    es.addEventListener('patch', e => {
      config.logger.debug('Received patch event');
      if (e && e.data) {
        let patch;
        try {
          patch = JSON.parse(e.data);
        } catch (err) {
          reportJsonError('patch', e.data);
          return;
        }
        for (const kind of Object.values(dataKind)) {
          const key = getKeyFromPath(kind, patch.path);
          if (key !== null) {
            config.logger.debug('Updating ' + key + ' in ' + kind.namespace);
            featureStore.upsert(kind, patch.data);
            break;
          }
        }
      } else {
        cb(new errors.LDStreamingError('Unexpected payload from event stream'));
      }
    });

    es.addEventListener('delete', e => {
      config.logger.debug('Received delete event');
      if (e && e.data) {
        let data;
        try {
          data = JSON.parse(e.data);
        } catch (err) {
          reportJsonError('delete', e.data);
          return;
        }
        const version = data.version;
        for (const kind of Object.values(dataKind)) {
          const key = getKeyFromPath(kind, data.path);
          if (key !== null) {
            config.logger.debug('Deleting ' + key + ' in ' + kind.namespace);
            featureStore.delete(kind, key, version);
            break;
          }
        }
      } else {
        cb(new errors.LDStreamingError('Unexpected payload from event stream'));
      }
    });
  };

  processor.stop = () => {
    if (es) {
      es.close();
    }
  };

  processor.close = () => {
    processor.stop();
  };

  return processor;
}

module.exports = StreamProcessor;
