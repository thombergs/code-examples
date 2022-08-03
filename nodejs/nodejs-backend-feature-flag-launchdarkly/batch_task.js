import chalk from 'chalk';
import LaunchDarkly from 'launchdarkly-node-server-sdk';
 
class BatchRunner {
 
	constructor( launchDarklyClient, flagKey, buyerKey ) { 
		this._launchDarklyClient = launchDarklyClient;
		this._flagKey = flagKey;
		this._buyerKey = buyerKey;
	}

	async run() {
 
		while ( true ) { 
			// We're going to check how many records we should be processing for 
			// each batch. This allows us to gradually ramp-up the batch size while
			// we monitor the mock database to see how it is handling the demand.
			var batchSize = await this._getBatchSize();
			var batch = await this._getNextBatch( batchSize );
			if ( ! batch.length ) { 
				break;
			}

			if (batchSize < 1000) {
				console.log( chalk.cyan( 'Processing', chalk.bold( batchSize, 'records.' ) ) ); 
			} else {
				console.log( chalk.redBright( 'Processing', chalk.bold( batchSize, 'records.' ) ) ); 
			}
			
			await this._processBatch( batch ); 
		}
 
	}

	async _getBatchSize() {
		await this._launchDarklyClient.waitForInitialization();
		var batchSize = await this._launchDarklyClient.variation(
			this._flagKey,
			{
				key: this._buyerKey
			},
			100 // Default fall-back variation value.
		); 
		return( batchSize ); 
	}

	async _getNextBatch( batchSize ) { 
		return( new Array( batchSize ).fill( 0 ) ); 
	}

	async _processBatch( batch ) { 
		await new Promise(
			( resolve, reject ) => { 
				setTimeout( resolve, 1000 ); 
			}
		); 
	} 
}

const LD_SDK_KEY = 'sdk-d2432dc7-e56a-458b-9f93-0361af47d578';
const LD_FLAG_KEY = 'tasks-batch-size-count';
const LD_BUYER_KEY = 'mob-b9d6d4d4-4300-46fa-9b13-d9eac89f9794';
const launchDarklyClient = LaunchDarkly.init( LD_SDK_KEY );
 
new BatchRunner( launchDarklyClient, LD_FLAG_KEY, LD_BUYER_KEY ).run();