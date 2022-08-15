import chalk from 'chalk';

class LdLogger {
 
	constructor ( ldClient, flagKey, user ) { 
		this.ldClient = ldClient;
		this.flagKey = flagKey;
		this.user = user;
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

		const minLogLevel = await this.ldClient.variation(
			this.flagKey,
			{
				key: this.user
			},
			'debug' // Default / fall-back value if LaunchDarkly unavailable.
		);
 
		if ( minLogLevel !== this.previousLevel ) { 
			console.log( chalk.bgGreen.bold.white( `Switching to log-level: ${ minLogLevel }` ) ); 
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

export default LdLogger;