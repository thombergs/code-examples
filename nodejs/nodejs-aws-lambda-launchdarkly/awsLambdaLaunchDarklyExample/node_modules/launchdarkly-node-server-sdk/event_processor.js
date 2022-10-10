const LRUCache = require('lru-cache');
const { v4: uuidv4 } = require('uuid');

const EventSummarizer = require('./event_summarizer');
const UserFilter = require('./user_filter');
const errors = require('./errors');
const httpUtils = require('./utils/httpUtils');
const messages = require('./messages');
const stringifyAttrs = require('./utils/stringifyAttrs');
const wrapPromiseCallback = require('./utils/wrapPromiseCallback');

const userAttrsToStringifyForEvents = [
  'key',
  'secondary',
  'ip',
  'country',
  'email',
  'firstName',
  'lastName',
  'avatar',
  'name',
];

function EventProcessor(sdkKey, config, errorReporter, diagnosticsManager) {
  const ep = {};

  const userFilter = UserFilter(config),
    summarizer = EventSummarizer(config),
    userKeysCache = new LRUCache({ max: config.userKeysCapacity }),
    mainEventsUri = config.eventsUri + '/bulk',
    diagnosticEventsUri = config.eventsUri + '/diagnostic';

  let queue = [],
    lastKnownPastTime = 0,
    droppedEvents = 0,
    deduplicatedUsers = 0,
    exceededCapacity = false,
    eventsInLastBatch = 0,
    shutdown = false,
    diagnosticsTimer;

  function enqueue(event) {
    if (queue.length < config.capacity) {
      queue.push(event);
      exceededCapacity = false;
    } else {
      if (!exceededCapacity) {
        exceededCapacity = true;
        config.logger.warn('Exceeded event queue capacity. Increase capacity to avoid dropping events.');
      }
      droppedEvents++;
    }
  }

  function shouldDebugEvent(event) {
    if (event.debugEventsUntilDate) {
      if (event.debugEventsUntilDate > lastKnownPastTime && event.debugEventsUntilDate > new Date().getTime()) {
        return true;
      }
    }
    return false;
  }

  function makeOutputEvent(event) {
    switch (event.kind) {
      case 'feature': {
        const debug = !!event.debug;
        const out = {
          kind: debug ? 'debug' : 'feature',
          creationDate: event.creationDate,
          key: event.key,
          value: event.value,
          default: event.default,
          prereqOf: event.prereqOf,
        };
        if (event.variation !== undefined && event.variation !== null) {
          out.variation = event.variation;
        }
        if (event.version !== undefined && event.version !== null) {
          out.version = event.version;
        }
        if (event.reason) {
          out.reason = event.reason;
        }
        if (config.inlineUsersInEvents || debug) {
          out.user = processUser(event);
        } else {
          out.userKey = getUserKey(event);
        }
        if (event.user && event.user.anonymous) {
          out.contextKind = 'anonymousUser';
        }
        return out;
      }
      case 'identify':
        return {
          kind: 'identify',
          creationDate: event.creationDate,
          key: getUserKey(event),
          user: processUser(event),
        };
      case 'custom': {
        const out = {
          kind: 'custom',
          creationDate: event.creationDate,
          key: event.key,
        };
        if (config.inlineUsersInEvents) {
          out.user = processUser(event);
        } else {
          out.userKey = getUserKey(event);
        }
        if (event.data !== null && event.data !== undefined) {
          out.data = event.data;
        }
        if (event.metricValue !== null && event.metricValue !== undefined) {
          out.metricValue = event.metricValue;
        }
        if (event.user && event.user.anonymous) {
          out.contextKind = 'anonymousUser';
        }
        return out;
      }
      default:
        return event;
    }
  }

  function processUser(event) {
    const filtered = userFilter.filterUser(event.user);
    return stringifyAttrs(filtered, userAttrsToStringifyForEvents);
  }

  function getUserKey(event) {
    return event.user && String(event.user.key);
  }

  ep.sendEvent = event => {
    let addIndexEvent = false,
      addFullEvent = false,
      addDebugEvent = false;

    if (shutdown) {
      return;
    }

    // Always record the event in the summarizer.
    summarizer.summarizeEvent(event);

    // Decide whether to add the event to the payload. Feature events may be added twice, once for
    // the event (if tracked) and once for debugging.
    if (event.kind === 'feature') {
      addFullEvent = event.trackEvents;
      addDebugEvent = shouldDebugEvent(event);
    } else {
      addFullEvent = true;
    }

    // For each user we haven't seen before, we add an index event - unless this is already
    // an identify event for that user.
    if (!addFullEvent || !config.inlineUsersInEvents) {
      if (event.user) {
        const isIdentify = event.kind === 'identify';
        if (userKeysCache.get(event.user.key)) {
          if (!isIdentify) {
            deduplicatedUsers++;
          }
        } else {
          userKeysCache.set(event.user.key, true);
          if (!isIdentify) {
            addIndexEvent = true;
          }
        }
      }
    }

    if (addIndexEvent) {
      enqueue({
        kind: 'index',
        creationDate: event.creationDate,
        user: processUser(event),
      });
    }
    if (addFullEvent) {
      enqueue(makeOutputEvent(event));
    }
    if (addDebugEvent) {
      const debugEvent = Object.assign({}, event, { debug: true });
      enqueue(makeOutputEvent(debugEvent));
    }
  };

  ep.flush = function (callback) {
    return wrapPromiseCallback(
      new Promise((resolve, reject) => {
        if (shutdown) {
          const err = new errors.LDInvalidSDKKeyError('Events cannot be posted because SDK key is invalid');
          reject(err);
          return;
        }

        const worklist = queue;
        queue = [];
        const summary = summarizer.getSummary();
        summarizer.clearSummary();
        if (Object.keys(summary.features).length) {
          summary.kind = 'summary';
          worklist.push(summary);
        }

        if (!worklist.length) {
          resolve();
          return;
        }

        eventsInLastBatch = worklist.length;
        config.logger.debug('Flushing %d events', worklist.length);

        tryPostingEvents(worklist, mainEventsUri, uuidv4(), resolve, reject, true);
      }),
      callback
    );
  };

  function tryPostingEvents(events, uri, payloadId, resolve, reject, canRetry) {
    const retryOrReject = err => {
      if (canRetry) {
        config.logger && config.logger.warn('Will retry posting events after 1 second');
        setTimeout(() => {
          tryPostingEvents(events, uri, payloadId, resolve, reject, false);
        }, 1000);
      } else {
        reject(err);
      }
    };

    const headers = Object.assign({ 'Content-Type': 'application/json' }, httpUtils.getDefaultHeaders(sdkKey, config));
    if (payloadId) {
      headers['X-LaunchDarkly-Payload-ID'] = payloadId;
      headers['X-LaunchDarkly-Event-Schema'] = '3';
    }

    const options = { method: 'POST', headers };
    const body = JSON.stringify(events);
    httpUtils.httpRequest(uri, options, body, config, (err, resp) => {
      if (err) {
        retryOrReject(err);
        return;
      }
      if (resp.headers['date']) {
        const date = Date.parse(resp.headers['date']);
        if (date) {
          lastKnownPastTime = date;
        }
      }
      if (resp.statusCode > 204) {
        const err = new errors.LDUnexpectedResponseError(
          messages.httpErrorMessage({ status: resp.statusCode }, 'event posting', 'some events were dropped')
        );
        errorReporter && errorReporter(err);
        if (!errors.isHttpErrorRecoverable(resp.statusCode)) {
          reject(err);
          shutdown = true;
        } else {
          retryOrReject(err);
        }
      } else {
        resolve();
      }
    });
  }

  function postDiagnosticEvent(event) {
    tryPostingEvents(
      event,
      diagnosticEventsUri,
      null,
      () => {},
      () => {},
      true
    );
  }

  const flushTimer = setInterval(() => {
    ep.flush().then(
      () => {},
      () => {}
    );
  }, config.flushInterval * 1000);

  const flushUsersTimer = setInterval(() => {
    userKeysCache.reset();
  }, config.userKeysFlushInterval * 1000);

  ep.close = () => {
    clearInterval(flushTimer);
    clearInterval(flushUsersTimer);
    diagnosticsTimer && clearInterval(diagnosticsTimer);
  };

  if (!config.diagnosticOptOut && diagnosticsManager) {
    const initEvent = diagnosticsManager.createInitEvent();
    postDiagnosticEvent(initEvent);

    diagnosticsTimer = setInterval(() => {
      const statsEvent = diagnosticsManager.createStatsEventAndReset(
        droppedEvents,
        deduplicatedUsers,
        eventsInLastBatch
      );
      droppedEvents = 0;
      deduplicatedUsers = 0;
      postDiagnosticEvent(statsEvent);
    }, config.diagnosticRecordingInterval * 1000);
  }

  return ep;
}

module.exports = EventProcessor;
