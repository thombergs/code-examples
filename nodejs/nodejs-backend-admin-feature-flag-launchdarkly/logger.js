import { format } from 'date-fns';
import padEnd from 'lodash/padEnd.js';
import capitalize from 'lodash/capitalize.js';

const LEVELS = { debug: 10, log: 20, warn: 30, error: 40 };
let LD_LOG_LEVEL = LEVELS['debug'];

class Logger {
  constructor(module) {
    this.module = module ? module : '';

    this.debug = this.debug.bind(this);
    this.log = this.log.bind(this);
    this.warn = this.warn.bind(this);
    this.error = this.error.bind(this);
    this.consoleWriter = this.consoleWriter.bind(this);
  }

  static setLogLevel(level) {
    LD_LOG_LEVEL = LEVELS[level];
  }

  static get(module) {
    return new Logger(module);
  }

  consoleWriter(level, message, context = '') {
    if (LEVELS[level] >= LD_LOG_LEVEL) {
      const dateTime = format(new Date(), 'MM-dd-yyyy HH:mm:ss:SSS');
      const formattedLevel = padEnd(capitalize(level), 5);
      const formattedMessage = `${dateTime} ${formattedLevel} [${
        this.module
      }] ${message}`;
      console[level](formattedMessage, context);
    }
  }

  debug(message, context) {
    this.consoleWriter('debug', message, context);
  }

  log(message, context) {
    this.consoleWriter('log', message, context);
  }

  warn(message, context) {
    this.consoleWriter('warn', message, context);
  }

  error(message, context) {
    this.consoleWriter('error', message, context);
  }
}

export default Logger;