import chalk from 'chalk';
import LaunchDarkly from 'launchdarkly-node-server-sdk';
import DynamicLogger from './dynamic_logger.js';

const LD_SDK_KEY = 'sdk-********-****-****-****-************';
const flagKey = 'backend-log-level';
const userName = 'admin';
const launchDarklyClient = LaunchDarkly.init( LD_SDK_KEY );
let logger;
let loop = 0;

launchDarklyClient.once('ready', async () => {
		setTimeout( executeLoop, 1000 ); 
	}
);

async function executeLoop () {
	logger = new DynamicLogger( 'DynamicLogging', launchDarklyClient, flagKey, userName );
	console.log( chalk.dim.italic( `Loop ${ ++loop }` ) ); 
	logger.debug( 'Executing loop.' );
	logger.debug('This is a debug log.');
	logger.info('This is an info log.');
	logger.warn('This is a warn log.');
	logger.error('This is a error log.');
	setTimeout( executeLoop, 1000 ); 
}