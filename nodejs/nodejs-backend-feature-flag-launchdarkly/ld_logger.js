import chalk from 'chalk';

class LdLogger {
 
	constructor ( ldClient, flagKey, buyerKey ) { 
		this.ldClient = ldClient;
		this.flagKey = flagKey;
		this.buyerKey = buyerKey;
		this.previousLevel = null; 
	}
 
	async debug( message ) { 
		if ( await this._presentLog( 'debug' ) ) {
			console.log( chalk.grey( chalk.bold( 'DEBUG:' ), message ) ); 
		} 
	}
 
	async error( message ) { 
		if ( await this._presentLog( 'error' ) ) {
			console.log( chalk.red( chalk.bold( 'ERROR:' ), message ) ); 
		} 
	}
 
	async info( message ) { 
		if ( await this._presentLog( 'info' ) ) {
			console.log( chalk.cyan( chalk.bold( 'INFO:' ), message ) ); 
		} 
	}
 
	async warn( message ) { 
		if ( await this._presentLog( 'warn' ) ) {
			console.log( chalk.magenta( chalk.bold( 'WARN:' ), message ) ); 
		}
	}
 
	async _presentLog( level ) {
 
		// Get the minimum log-level from the given LaunchDarkly client. Since this is
		// an OPERATIONAL flag, not a USER flag, the "key" needs to indicate the current
		// application context. In this case, I'm calling the app, "backend-log-level". If I
		// want to get more granular, I could use something like machine ID. But, for
		// this particular setting, I think app-name makes sense.
		const minLogLevel = await this.ldClient.variation(
			this.flagKey,
			{
				key: this.buyerKey
			},
			'debug' // Default / fall-back value if LaunchDarkly unavailable.
		);
 
		if ( minLogLevel !== this.previousLevel ) { 
			console.log( chalk.bgGreen.bold.white( `Switching to log-level: ${ minLogLevel }` ) ); 
		}
 
		// Given the minimum log level, determine if the level in question can be logged.
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

export default LdLogger;