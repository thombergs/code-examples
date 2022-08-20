import chalk from 'chalk';
import util from 'util';
import LaunchDarkly from 'launchdarkly-node-server-sdk';
import DynamicLogger from './dynamic_logger.js';

const LD_SDK_KEY = 'sdk-********-****-****-****-************';
const flagKey = 'backend-log-level';
const userName = 'admin';
const launchDarklyClient = LaunchDarkly.init( LD_SDK_KEY );
const asyncGetFlag = util.promisify(launchDarklyClient.variation);
const user = {
    user: userName
};
let logger;
let loop = 0;

launchDarklyClient.once('ready', async () => {
		setTimeout( executeLoop, 1000 ); 
	}
);

async function executeLoop () {	
	const initialLogLevel = await asyncGetFlag(flagKey, user, 'debug');
	logger = new DynamicLogger( 'DynamicLogging', launchDarklyClient, flagKey, userName );
	DynamicLogger.setLogLevel(initialLogLevel);	
	console.log( chalk.dim.italic( `Loop ${ ++loop }` ) ); 
	logger.debug( 'Executing loop.' );
	logger.debug('This is a debug log.');
	logger.info('This is an info log.');
	logger.warn('This is a warn log.');
	logger.error('This is a error log.');
	setTimeout( executeLoop, 1000 ); 
}