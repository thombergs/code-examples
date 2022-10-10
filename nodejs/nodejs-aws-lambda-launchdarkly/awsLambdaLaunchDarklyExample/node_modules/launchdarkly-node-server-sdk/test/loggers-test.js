const loggers = require('../loggers');

describe('basicLogger', () => {
  it('uses console.error by default', () => {
    const realConsoleError = console.error;
    const mock = jest.fn();
    try {
      console.error = mock;
      const logger = loggers.basicLogger();
      logger.warn('hello');
      expect(mock).toHaveBeenCalledWith('warn: [LaunchDarkly] hello');
    } finally {
      console.error = realConsoleError;
    }
  });

  it('can write to an arbitrary function', () => {
    const outputFn = jest.fn();
    const logger = loggers.basicLogger({ destination: outputFn });
    logger.warn('hello');
    expect(outputFn).toHaveBeenCalledWith('warn: [LaunchDarkly] hello');
  });

  it('throws an exception immediately if destination is not a function', () => {
    expect(() => loggers.basicLogger({ destination: 'Mars' })).toThrow();
  });

  it('does not use util.format if there is only one argument', () => {
    const outputFn = jest.fn();
    const logger = loggers.basicLogger({ destination: outputFn });
    logger.warn('%d things');
    expect(outputFn).toHaveBeenCalledWith('warn: [LaunchDarkly] %d things');
  });

  it('does not use util.format if there are multiple arguments', () => {
    const outputFn = jest.fn();
    const logger = loggers.basicLogger({ destination: outputFn });
    logger.warn('%d things', 3);
    expect(outputFn).toHaveBeenCalledWith('warn: [LaunchDarkly] 3 things');
  });

  describe('output filtering by level', () => {
    const testLevel = (minLevel, enabledLevels) => {
      it('level: ' + minLevel, () => {
        const outputFn = jest.fn();
        const config = { destination: outputFn };
        if (minLevel) {
          config.level = minLevel;
        }
        const logger = loggers.basicLogger({ level: minLevel, destination: outputFn });
        logger.debug('some debug output');
        logger.info('some info output');
        logger.warn('some warn output');
        logger.error('some error output');
        for (const [level, shouldBeEnabled] of Object.entries(enabledLevels)) {
          const line = level + ': [LaunchDarkly] some ' + level + ' output';
          if (shouldBeEnabled) {
            expect(outputFn).toHaveBeenCalledWith(line);
          } else {
            expect(outputFn).not.toHaveBeenCalledWith(line);
          }
        }
      });
    };

    testLevel('debug', { 'debug': true, 'info': true, 'warn': true, 'error': true });
    testLevel('info', { 'debug': false, 'info': true, 'warn': true, 'error': true });
    testLevel('warn', { 'debug': false, 'info': false, 'warn': true, 'error': true });
    testLevel('error', { 'debug': false, 'info': false, 'warn': false, 'error': true });
    testLevel('none', { 'debug': false, 'info': false, 'warn': false, 'error': false });

    // default is info
    testLevel(undefined, { 'debug': false, 'info': true, 'warn': true, 'error': true });
  });
});

describe('safeLogger', () => {
  function mockLogger() {
    return {
      error: jest.fn(),
      warn: jest.fn(),
      info: jest.fn(),
      debug: jest.fn(),
    };
  }

  const levels = ['error', 'warn', 'info', 'debug'];

  it('throws an error if you pass in a logger that does not conform to the LDLogger schema', () => {
    const fallbackLogger = mockLogger();

    // If the method does not exist
    levels.forEach(method => {
      const logger = mockLogger();
      delete logger[method];
      expect(() => loggers.safeLogger(logger, fallbackLogger)).toThrow(/Provided logger instance must support .* method/);
    });

    // If the method is not a function
    levels.forEach(method => {
      const logger = mockLogger();
      logger[method] = 'invalid';
      expect(() => loggers.safeLogger(logger, fallbackLogger)).toThrow(/Provided logger instance must support .* method/);
    });
  });

  it('If a logger method throws an error, the error is caught and logged, then the fallback logger is called', () => {
    const err = Error('Something bad happened');

    levels.forEach(level => {
      const logger = mockLogger();
      logger[level] = jest.fn(() => {
        throw err
      });
      const fallbackLogger = mockLogger();
      const wrappedLogger = loggers.safeLogger(logger, fallbackLogger);

      expect(() => wrappedLogger[level]('this is a logline', 'with multiple', 'arguments')).not.toThrow();

      expect(fallbackLogger.error).toHaveBeenNthCalledWith(1, 'Error calling provided logger instance method ' + level + ': ' + err);

      const nthCall = level === 'error' ? 2 : 1;
      expect(fallbackLogger[level]).toHaveBeenNthCalledWith(nthCall, 'this is a logline', 'with multiple', 'arguments');
    });
  });
});
