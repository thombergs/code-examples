import cron from 'cron';
import LaunchDarkly from 'launchdarkly-node-server-sdk';

const CronJob = cron.CronJob;
const CronTime = cron.CronTime;

// Initiating LaunchDarkly Client
const LD_SDK_KEY = 'sdk-********-****-****-****-************';
const userName = 'admin';
const launchDarklyClient = LaunchDarkly.init( LD_SDK_KEY );

launchDarklyClient.once('ready', async () => {    
    const cronConfig = await launchDarklyClient.variation(
      'cron-config',
      {
          key: userName
      },
      '*/4 * * * *' // Default fall-back variation value.
    );

    const job = new CronJob(cronConfig, function() {
      run();
    }, null, false)

    let run = () => {
      console.log('scheduled task called');
    }

    let scheduler = () => {
      console.log('CRON JOB STARTED WILL RUN AS PER LAUNCHDARKLY CONFIG');
      job.start();
    }

    let schedulerStop = () => {
      job.stop();
      console.log('scheduler stopped');
    }

    let schedulerStatus = () => {
      console.log('cron status ---->>>', job.running);
    }

    let changeTime = (input) => {
      job.setTime(new CronTime(input));
      console.log('changed to every 1 second');
    }

    scheduler();
    setTimeout(() => {schedulerStatus()}, 1000);
    setTimeout(() => {schedulerStop()}, 9000);
    setTimeout(() => {schedulerStatus()}, 10000);
    setTimeout(() => {changeTime('* * * * * *')}, 11000);
    setTimeout(() => {scheduler()}, 12000);
    setTimeout(() => {schedulerStop()}, 16000);
  }
);