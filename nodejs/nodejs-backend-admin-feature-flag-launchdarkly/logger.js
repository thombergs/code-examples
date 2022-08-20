import { format } from 'date-fns';
import padEnd from 'lodash/padEnd.js';
import capitalize from 'lodash/capitalize.js';

const LEVELS = { debug: 10, log: 20, warn: 30, error: 40 };
let currentLogLevel = LEVELS['debug'];

class Logger {
  constructor(module) {
    this.module = module ? module : '';

    this.debug = this.debug.bind(this);
    this.log = this.log.bind(this);
    this.warn = this.warn.bind(this);
    this.error = this.error.bind(this);
    this.writeToConsole = this.writeToConsole.bind(this);
  }

  static setLogLevel(level) {
    currentLogLevel = LEVELS[level];
  }

  static get(module) {
    return new Logger(module);
  }

  writeToConsole(level, message, context = '') {
    if (LEVELS[level] >= currentLogLevel) {
      const dateTime = format(new Date(), 'MM-dd-yyyy HH:mm:ss:SSS');
      const formattedLevel = padEnd(capitalize(level), 5);
      const formattedMessage = `${dateTime} ${formattedLevel} [${
        this.module
      }] ${message}`;
      console[level](formattedMessage, context);
    }
  }

  debug(message, context) {
    this.writeToConsole('debug', message, context);
  }

  log(message, context) {
    this.writeToConsole('log', message, context);
  }

  warn(message, context) {
    this.writeToConsole('warn', message, context);
  }

  error(message, context) {
    this.writeToConsole('error', message, context);
  }
}

export default Logger;