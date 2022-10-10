const errors = require('./errors');
const messages = require('./messages');
const dataKind = require('./versioned_data_kind');

function PollingProcessor(config, requestor) {
  const processor = {},
    featureStore = config.featureStore,
    intervalMs = config.pollInterval * 1000;

  let stopped = false;

  let pollTask;

  function poll(maybeCallback) {
    const cb = maybeCallback || function () {};

    if (stopped) {
      return;
    }

    config.logger.debug('Polling LaunchDarkly for feature flag updates');

    requestor.requestAllData((err, respBody) => {
      if (err) {
        if (err.status && !errors.isHttpErrorRecoverable(err.status)) {
          const message = messages.httpErrorMessage(err, 'polling request');
          config.logger.error(message);
          cb(new errors.LDPollingError(message));
          processor.stop();
        } else {
          config.logger.warn(messages.httpErrorMessage(err, 'polling request', 'will retry'));
        }
      } else {
        if (respBody) {
          const allData = JSON.parse(respBody);
          const initData = {};
          initData[dataKind.features.namespace] = allData.flags;
          initData[dataKind.segments.namespace] = allData.segments;
          featureStore.init(initData, () => {
            cb();
          });
        }
        // There wasn't an error but there wasn't any new data either, so just keep polling
      }
    });
  }

  processor.start = cb => {
    if (!pollTask && !stopped) {
      pollTask = setInterval(() => poll(cb), intervalMs);
      // setInterval always waits for the delay before firing the first time, but we want to do an initial poll right away
      poll(cb);
    }
  };

  processor.stop = () => {
    stopped = true;
    if (pollTask) {
      clearInterval(pollTask);
    }
  };

  processor.close = () => {
    processor.stop();
  };

  return processor;
}

module.exports = PollingProcessor;
