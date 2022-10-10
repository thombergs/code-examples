const ld = require('launchdarkly-node-server-sdk');

const { Log, sdkLogger } = require('./log');

const badCommandError = new Error('unsupported command');

function makeSdkConfig(options, tag) {
  const cf = {
    logger: sdkLogger(tag),
  };
  const maybeTime = seconds => (seconds === undefined || seconds === null ? undefined : seconds / 1000);
  if (options.streaming) {
    cf.streamUri = options.streaming.baseUri;
    cf.streamInitialReconnectDelay = maybeTime(options.streaming.initialRetryDelayMs);
  }
  if (options.events) {
    cf.allAttributesPrivate = options.events.allAttributesPrivate;
    cf.eventsUri = options.events.baseUri;
    cf.capacity = options.events.capacity;
    cf.diagnosticOptOut = !options.events.enableDiagnostics;
    cf.flushInterval = maybeTime(options.events.flushIntervalMs);
    cf.inlineUsersInEvents = options.events.inlineUsers;
    cf.privateAttributeNames = options.events.globalPrivateAttributes;
  }
  if (options.tags) {
    cf.application = {
      id: options.tags.applicationId,
      version: options.tags.applicationVersion,
    };
  }
  return cf;
}

async function newSdkClientEntity(options) {
  const c = {};
  const log = Log(options.tag);

  log.info('Creating client with configuration: ' + JSON.stringify(options.configuration));
  const timeout =
    options.configuration.startWaitTimeMs !== null && options.configuration.startWaitTimeMs !== undefined
      ? options.configuration.startWaitTimeMs
      : 5000;
  const client = ld.init(
    options.configuration.credential || 'unknown-sdk-key',
    makeSdkConfig(options.configuration, options.tag)
  );
  try {
    await Promise.race([client.waitForInitialization(), new Promise(resolve => setTimeout(resolve, timeout))]);
  } catch (_) {
    // if waitForInitialization() rejects, the client failed to initialize, see next line
  }
  if (!client.initialized() && !options.configuration.initCanFail) {
    client.close();
    throw new Error('client initialization failed');
  }

  c.close = () => {
    client.close();
    log.info('Test ended');
  };

  c.doCommand = async params => {
    log.info('Received command: ' + params.command);
    switch (params.command) {
      case 'evaluate': {
        const pe = params.evaluate;
        if (pe.detail) {
          return await client.variationDetail(pe.flagKey, pe.user, pe.defaultValue);
        } else {
          const value = await client.variation(pe.flagKey, pe.user, pe.defaultValue);
          return { value };
        }
      }

      case 'evaluateAll': {
        const pea = params.evaluateAll;
        const eao = {
          clientSideOnly: pea.clientSideOnly,
          detailsOnlyForTrackedFlags: pea.detailsOnlyForTrackedFlags,
          withReasons: pea.withReasons,
        };
        return { state: await client.allFlagsState(pea.user, eao) };
      }

      case 'identifyEvent':
        client.identify(params.identifyEvent.user);
        return undefined;

      case 'customEvent': {
        const pce = params.customEvent;
        client.track(pce.eventKey, pce.user, pce.data, pce.metricValue);
        return undefined;
      }

      case 'aliasEvent':
        client.alias(params.aliasEvent.user, params.aliasEvent.previousUser);
        return undefined;

      case 'flushEvents':
        client.flush();
        return undefined;

      case 'getBigSegmentStoreStatus':
        return undefined;

      default:
        throw badCommandError;
    }
  };

  return c;
}

module.exports.newSdkClientEntity = newSdkClientEntity;
module.exports.badCommandError = badCommandError;
