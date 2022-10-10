const InMemoryFeatureStore = require('./feature_store');
const loggers = require('./loggers');
const messages = require('./messages');

module.exports = (function () {
  const defaults = function () {
    return {
      baseUri: 'https://app.launchdarkly.com',
      streamUri: 'https://stream.launchdarkly.com',
      eventsUri: 'https://events.launchdarkly.com',
      stream: true,
      streamInitialReconnectDelay: 1,
      sendEvents: true,
      timeout: 5,
      capacity: 10000,
      flushInterval: 5,
      pollInterval: 30,
      offline: false,
      useLdd: false,
      allAttributesPrivate: false,
      privateAttributeNames: [],
      inlineUsersInEvents: false,
      userKeysCapacity: 1000,
      userKeysFlushInterval: 300,
      diagnosticOptOut: false,
      diagnosticRecordingInterval: 900,
      featureStore: InMemoryFeatureStore(),
    };
  };

  const typesForPropertiesWithNoDefault = {
    // Add a value here if we add a configuration property whose type cannot be determined by looking
    // in baseDefaults (for instance, the default is null but if the value isn't null it should be a
    // string). The allowable values are 'boolean', 'string', 'number', 'object', 'function', 'array' or
    // 'factory' (the last one means it can be either a function or an object).
    bigSegments: 'object',
    eventProcessor: 'object',
    featureStore: 'object',
    logger: 'object', // LDLogger
    proxyAgent: 'object',
    proxyAuth: 'string',
    proxyHost: 'string',
    proxyPort: 'number',
    proxyScheme: 'string',
    tlsParams: 'object', // LDTLSOptions
    updateProcessor: 'factory', // gets special handling in validation
    wrapperName: 'string',
    wrapperVersion: 'string',
  };

  /**
   * Expression to validate characters that are allowed in tag keys and values.
   */
  const allowedTagCharacters = /^(\w|\.|-)+$/;

  /**
   * Verify that a value meets the requirements for a tag value.
   * @param {Object} config
   * @param {string} tagValue
   */
  function validateTagValue(name, config, tagValue) {
    if (typeof tagValue !== 'string' || !tagValue.match(allowedTagCharacters)) {
      config.logger.warn(messages.invalidTagValue(name));
      return undefined;
    }
    if (tagValue.length > 64) {
      config.logger.warn(messages.tagValueTooLong(name));
      return undefined;
    }
    return tagValue;
  }

  const optionsWithValidatorsOrConversions = {
    // Add a value here if we add a configuration property which requires custom validation
    // and/or type conversion.
    // The validator should log a message for any validation issues encountered.
    // The validator should return undefined, or the validated value.

    application: (name, config, value) => {
      const validated = {};
      if (value.id) {
        validated.id = validateTagValue(`${name}.id`, config, value.id);
      }
      if (value.version) {
        validated.version = validateTagValue(`${name}.version`, config, value.version);
      }
      return validated;
    },
  };

  /* eslint-disable camelcase */
  const deprecatedOptions = {};
  /* eslint-enable camelcase */

  function checkDeprecatedOptions(configIn) {
    const config = configIn;
    Object.keys(deprecatedOptions).forEach(oldName => {
      if (config[oldName] !== undefined) {
        const newName = deprecatedOptions[oldName];
        config.logger.warn(messages.deprecated(oldName, newName));
        if (config[newName] === undefined) {
          config[newName] = config[oldName];
        }
        delete config[oldName];
      }
    });
  }

  function applyDefaults(config, defaults) {
    // This works differently from Object.assign() in that it will *not* override a default value
    // if the provided value is explicitly set to null.
    const ret = Object.assign({}, config);
    Object.keys(defaults).forEach(name => {
      if (ret[name] === undefined || ret[name] === null) {
        ret[name] = defaults[name];
      }
    });
    return ret;
  }

  function canonicalizeUri(uri) {
    return uri.replace(/\/+$/, '');
  }

  function validateTypesAndNames(configIn, defaultConfig) {
    const config = configIn;
    const typeDescForValue = value => {
      if (value === null || value === undefined) {
        return undefined;
      }
      if (Array.isArray(value)) {
        return 'array';
      }
      const t = typeof value;
      if (t === 'boolean' || t === 'string' || t === 'number') {
        return t;
      }
      return 'object';
    };
    Object.keys(config).forEach(name => {
      const value = config[name];
      if (value !== null && value !== undefined) {
        const defaultValue = defaultConfig[name];
        const typeDesc = typesForPropertiesWithNoDefault[name];
        const validator = optionsWithValidatorsOrConversions[name];
        if (defaultValue === undefined && typeDesc === undefined && validator === undefined) {
          config.logger.warn(messages.unknownOption(name));
        } else if (validator !== undefined) {
          const validated = validator(name, config, config[name]);
          if (validated !== undefined) {
            config[name] = validated;
          } else {
            delete config[name];
          }
        } else {
          const expectedType = typeDesc || typeDescForValue(defaultValue);
          const actualType = typeDescForValue(value);
          if (actualType !== expectedType) {
            if (expectedType === 'factory' && (typeof value === 'function' || typeof value === 'object')) {
              // for some properties, we allow either a factory function or an instance
              return;
            }
            if (expectedType === 'boolean') {
              config[name] = !!value;
              config.logger.warn(messages.wrongOptionTypeBoolean(name, actualType));
            } else {
              config.logger.warn(messages.wrongOptionType(name, expectedType, actualType));
              config[name] = defaultConfig[name];
            }
          }
        }
      }
    });
  }

  function enforceMinimum(configIn, name, min) {
    const config = configIn;
    if (config[name] < min) {
      config.logger.warn(messages.optionBelowMinimum(name, config[name], min));
      config[name] = min;
    }
  }

  function validate(options) {
    let config = Object.assign({}, options || {});

    const fallbackLogger = loggers.basicLogger({ level: 'info' });
    config.logger = config.logger ? loggers.safeLogger(config.logger, fallbackLogger) : fallbackLogger;

    checkDeprecatedOptions(config);

    const defaultConfig = defaults();
    config = applyDefaults(config, defaultConfig);

    validateTypesAndNames(config, defaultConfig);

    config.baseUri = canonicalizeUri(config.baseUri);
    config.streamUri = canonicalizeUri(config.streamUri);
    config.eventsUri = canonicalizeUri(config.eventsUri);

    enforceMinimum(config, 'pollInterval', 30);
    enforceMinimum(config, 'diagnosticRecordingInterval', 60);

    return config;
  }

  /**
   * Get tags for the specified configuration.
   *
   * If any additional tags are added to the configuration, then the tags from
   * this method should be extended with those.
   * @param {Object} config The already valiated configuration.
   * @returns {Object} The tag configuration.
   */
  function getTags(config) {
    const tags = {};
    if (config.application && config.application.id !== undefined && config.application.id !== null) {
      tags['application-id'] = [config.application.id];
    }
    if (config.application && config.application.version !== undefined && config.application.id !== null) {
      tags['application-version'] = [config.application.version];
    }

    return tags;
  }

  return {
    validate,
    defaults,
    getTags,
  };
})();
