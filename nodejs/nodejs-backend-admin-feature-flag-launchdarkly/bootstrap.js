import util from 'util';
import express from 'express';
import LaunchDarkly from 'launchdarkly-node-server-sdk';
import Logger from './logger.js';

const PORT = 5000;
const app = express();
const simpleLogger = new Logger('SimpleLogging');

const LD_SDK_KEY = 'sdk-********-****-****-****-************';
const LOG_LEVEL_FLAG_KEY = 'backend-log-level';
const client = LaunchDarkly.init(LD_SDK_KEY);
const asyncGetFlag = util.promisify(client.variation);

client.once('ready', async () => {
  const user = {
    anonymous: true
  };
  const initialLogLevel = await asyncGetFlag(LOG_LEVEL_FLAG_KEY, user, 'debug');
  Logger.setLogLevel(initialLogLevel);

  app.get('/', (req, res) => {
    simpleLogger.debug('detailed debug message');
    simpleLogger.log('simple log message');
    simpleLogger.warn('Warning warning do something');
    simpleLogger.error('ERROR! ERROR!');
    res.sendStatus(200);
  });

  app.listen(PORT, () => {
    simpleLogger.log(`Server listening on port ${PORT}`);
  });
});