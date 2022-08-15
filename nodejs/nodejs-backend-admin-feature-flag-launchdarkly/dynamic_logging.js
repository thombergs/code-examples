import chalk from 'chalk';
import LaunchDarkly from 'launchdarkly-node-server-sdk';
import LdLogger from './ld_logger.js';

const LD_SDK_KEY = 'sdk-d2432dc7-e56a-458b-9f93-0361af47d578';
const flagKey = 'backend-log-level';
const userName = 'admin';
const launchDarklyClient = LaunchDarkly.init( LD_SDK_KEY );
const logger = new LdLogger( launchDarklyClient, flagKey, userName );
let loop = 0;

launchDarklyClient.once('ready', async () => { 
		setTimeout( executeLoop, 1000 ); 
	}
);

//Fake node status reader randomized to throw errors.
function readNodeStatus() { 
	const nodeStatus = ( Math.random() * 100 ).toFixed( 1 ); 
	if ( nodeStatus <= 30 ) { 
		throw new Error( 'IOError' ); 
	} 
	return nodeStatus; 
}

function executeLoop () {
		console.log( chalk.dim.italic( `Loop ${ ++loop }` ) ); 
		logger.debug( 'Executing loop.' ); 
		try { 
			logger.debug( 'Checking number of nodes that are busy.' );
			const nodeCount = readNodeStatus();
			logger.info( `Number of Nodes that are busy: ${ nodeCount }%` ); 
			if ( nodeCount >= 50 ) { 
				logger.warn( 'More than half of the nodes are busy. We should'
				+ ' think of adding more nodes to the cluster.' ); 
			} 
		} catch ( error ) { 
			logger.error( `Node count could not be read: ${ error.message }` );
		} 
		setTimeout( executeLoop, 1000 ); 
}