import chalk from 'chalk';
import LaunchDarkly from 'launchdarkly-node-server-sdk';
import LdLogger from './ld_logger.js';

const LD_SDK_KEY = 'sdk-d2432dc7-e56a-458b-9f93-0361af47d578';
const flagKey = 'backend-log-level';
const buyerKey = 'mob-b9d6d4d4-4300-46fa-9b13-d9eac89f9794';
const launchDarklyClient = LaunchDarkly.init( LD_SDK_KEY );
launchDarklyClient.waitForInitialization();
const logger = new LdLogger( launchDarklyClient, flagKey, buyerKey );
let loop = 0;
 
launchDarklyClient.once('ready', async () => { 
		setTimeout( executeLoop, 1000 ); 
	}
);
 
//Fake memory reader randomized to throw errors.
function readMemory() { 
	const memory = ( Math.random() * 100 ).toFixed( 1 ); 
	if ( memory <= 30 ) { 
		throw new Error( 'IOError' ); 
	} 
	return memory; 
}

function executeLoop () {
		console.log( chalk.dim.italic( `Loop ${ ++loop }` ) ); 
		logger.debug( 'Executing loop.' ); 
		try { 
			logger.debug( 'Checking free memory.' );
			const memoryUsed = readMemory();
			logger.info( `Memory used: ${ memoryUsed }%` ); 
			if ( memoryUsed >= 50 ) { 
				logger.warn( 'More than half of free memory has been allocated.' ); 
			} 
		} catch ( error ) { 
			logger.error( `Memory could not be read: ${ error.message }` );
		} 
		setTimeout( executeLoop, 1000 ); 
}