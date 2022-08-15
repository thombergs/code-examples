import util from 'util';
import express from 'express';
import LaunchDarkly from 'launchdarkly-node-server-sdk';
import Logger from './logger.js';

const PORT = 5000;
const app = express();
const simpleLogger = new Logger('SimpleLogging');

const LD_SDK_KEY = 'sdk-d2432dc7-e56a-458b-9f93-0361af47d578';
const LOG_LEVEL_FLAG_KEY = 'backend-log-level';
const userName = 'admin';
const client = LaunchDarkly.init(LD_SDK_KEY);
client.waitForInitialization();
const asyncGetFlag = util.promisify(client.variation);

const subscribeToChanges = () => {
  client.on(`update:${LOG_LEVEL_FLAG_KEY}`, (_, newValue) => {
    const {
      fallthrough: { variation },
      variations
    } = newValue;
    const newLogLevel = variations[variation];
    console.log(`${LOG_LEVEL_FLAG_KEY} updated to '${newLogLevel}'`);
    Logger.setLogLevel(newLogLevel);
  });
};

client.once('ready', async () => {
  const user = {
    key: userName,
    anonymous: true
  };
  const initialLogLevel = await asyncGetFlag(LOG_LEVEL_FLAG_KEY, user, 'debug');
  Logger.setLogLevel(initialLogLevel);

  subscribeToChanges();

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