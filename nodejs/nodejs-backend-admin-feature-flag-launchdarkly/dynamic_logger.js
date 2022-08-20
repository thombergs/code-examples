import { format } from 'date-fns';
import padEnd from 'lodash/padEnd.js';
import capitalize from 'lodash/capitalize.js';

const LEVELS = { debug: 10, log: 20, warn: 30, error: 40 };
let currentLogLevel = LEVELS['debug'];

class DynamicLogger {
  constructor( module, ldClient, flagKey, user ) {
    this.module = module ? module : '';

    this.debug = this.debug.bind(this);
    this.info = this.info.bind(this);
    this.warn = this.warn.bind(this);
    this.error = this.error.bind(this);
    this.writeToConsole = this.writeToConsole.bind(this);
    this.ldClient = ldClient;
	this.flagKey = flagKey;
	this.user = user;
	this.previousLevel = null; 
  }

  static setLogLevel(level) {
    currentLogLevel = LEVELS[level];
  }

  static get(module) {
    return new Logger(module);
  }

  writeToConsole(level, message) {
    if (LEVELS[level] >= currentLogLevel) {
      const dateTime = format(new Date(), 'MM-dd-yyyy HH:mm:ss:SSS');
      const formattedLevel = padEnd(capitalize(level), 5);
      const formattedMessage = `${dateTime} ${formattedLevel} [${
        this.module
      }] ${message}`;
      console[level](formattedMessage, '');
    }
  }

  async debug( message ) { 
    if ( await this._presentLog( 'debug' ) ) {
        this.writeToConsole('debug', message); 
    } 
  }

  async error( message ) { 
    if ( await this._presentLog( 'error' ) ) {
        this.writeToConsole('error', message); 
    } 
  }

  async info( message ) { 
    if ( await this._presentLog( 'info' ) ) {
        this.writeToConsole('info', message); 
    } 
  }

  async warn( message ) { 
    if ( await this._presentLog( 'warn' ) ) {
        this.writeToConsole('warn', message); 
    }
  }

  async _presentLog( level ) {

    const minLogLevel = await this.ldClient.variation(
        this.flagKey,
        {
           key: this.user
        },
        'debug' // Default / fall-back value if LaunchDarkly unavailable.
    );

    if ( minLogLevel !== this.previousLevel ) { 
       console.log( `Switching to log-level: ${ minLogLevel }` ); 
    }
        
    switch ( this.previousLevel = minLogLevel ) {
        case 'error':
            return level === 'error';
        case 'warn':
            return level === 'error' ||	level === 'warn';
        case 'info':
            return level === 'error' || level === 'warn' || level === 'info';
        default:
            return true;
        } 
    }
}

export default DynamicLogger;