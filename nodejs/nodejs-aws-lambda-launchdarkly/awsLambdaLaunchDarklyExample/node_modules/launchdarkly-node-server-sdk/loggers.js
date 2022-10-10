const util = require('util');

const logLevels = ['debug', 'info', 'warn', 'error', 'none'];

/**
 * A simple logger that writes to stderr. See index.d.ts
 */
function basicLogger(options) {
  const destination = (options && options.destination) || console.error;
  if (typeof destination !== 'function') {
    throw new Error('destination for basicLogger was set to a non-function');
  }

  let minLevel = 1; // default is 'info'
  if (options && options.level) {
    for (let i = 0; i < logLevels.length; i++) {
      if (logLevels[i] === options.level) {
        minLevel = i;
      }
    }
  }

  function write(prefix, args) {
    if (args.length < 1) {
      return;
    }
    let line;
    if (args.length === 1) {
      line = prefix + args[0];
    } else {
      const tempArgs = [...args];
      tempArgs[0] = prefix + tempArgs[0];
      line = util.format(...tempArgs);
    }
    destination(line);
  }

  const logger = {};
  for (let i = 0; i < logLevels.length; i++) {
    const levelName = logLevels[i];
    if (levelName !== 'none') {
      if (i < minLevel) {
        logger[levelName] = () => {};
      } else {
        const prefix = levelName + ': [LaunchDarkly] ';
        logger[levelName] = function () {
          // can't use arrow function with "arguments"
          write(prefix, arguments);
        };
      }
    }
  }

  return logger;
}

/**
 * Returns a logger that does nothing.
 */
function nullLogger() {
  return {
    debug: () => {},
    info: () => {},
    warn: () => {},
    error: () => {},
  };
}

// The safeLogger logic exists because we allow the application to pass in a custom logger, but
// there is no guarantee that the logger works correctly and if it ever throws exceptions there
// could be serious consequences (e.g. an uncaught exception within an error event handler, due
// to the SDK trying to log the error, can terminate the application). An exception could result
// from faulty logic in the logger implementation, or it could be that this is not a logger at
// all but some other kind of object; the former is handled by a catch block that logs an error
// message to the SDK's default logger, and we can at least partly guard against the latter by
// checking for the presence of required methods at configuration time.

/**
 * Asserts that the caller-supplied logger contains all required methods
 * and wraps it in an exception handler that falls back to the fallbackLogger.
 * @param {LDLogger} logger
 * @param {LDLogger} fallbackLogger
 */
function safeLogger(logger, fallbackLogger) {
  validateLogger(logger);

  const wrappedLogger = {};
  logLevels.forEach(level => {
    if (level !== 'none') {
      wrappedLogger[level] = wrapLoggerLevel(logger, fallbackLogger, level);
    }
  });

  return wrappedLogger;
}

function validateLogger(logger) {
  logLevels.forEach(level => {
    if (level !== 'none' && (!logger[level] || typeof logger[level] !== 'function')) {
      throw new Error('Provided logger instance must support logger.' + level + '(...) method');
      // Note that the SDK normally does not throw exceptions to the application, but that rule
      // does not apply to LDClient.init() which will throw an exception if the parameters are so
      // invalid that we cannot proceed with creating the client. An invalid logger meets those
      // criteria since the SDK calls the logger during nearly all of its operations.
    }
  });
}

function wrapLoggerLevel(logger, fallbackLogger, level) {
  const logFn = logger[level];
  return function wrappedLoggerMethod() {
    try {
      return logFn.apply(logger, arguments);
    } catch (err) {
      fallbackLogger.error('Error calling provided logger instance method ' + level + ': ' + err);
      fallbackLogger[level].apply(fallbackLogger, arguments);
    }
  };
}

module.exports = {
  basicLogger,
  nullLogger,
  safeLogger,
};
