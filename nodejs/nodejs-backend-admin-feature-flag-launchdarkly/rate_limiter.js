import bodyParser from 'body-parser';
import express from 'express';
import cors from 'cors';
import rateLimit from 'express-rate-limit';
import LaunchDarkly from 'launchdarkly-node-server-sdk';
import LdLogger from './ld_logger.js';

// Initiating LaunchDarkly Client
const LD_SDK_KEY = 'sdk-d2432dc7-e56a-458b-9f93-0361af47d578';
const userName = 'admin';
const launchDarklyClient = LaunchDarkly.init( LD_SDK_KEY );

// Initiating the Logger
const flagKey = 'backend-log-level';
let logger;
launchDarklyClient.once('ready', async () => {
    logger = new LdLogger( launchDarklyClient, flagKey, userName );
    serverInit();
  }
);

const serverInit = async () => {

    // Essential globals
    const app = express();

    //  Initialize global application middlewares
    app.use(cors());
    app.use(
        bodyParser.urlencoded({
            extended: true
        })
    );
    app.use(
        bodyParser.json({
            type: 'application/json'
        })
    );
    
    // Initialize Rate Limit Midlleware
    const rateLimiterConfig = await launchDarklyClient.variation(
        'rate-limiter-config',
        {
            key: userName // The static "user" for this task.
        },
        {
            windowMs: 24 * 60 * 60 * 1000, // 24 hrs in milliseconds
            max: 100, // Limit each IP to 100 requests per `window`
            // (here, per 24 hours). Set it to 0 to disable rateLimiter
            message: 'You have exceeded 100 requests in 24 hrs limit!', 
            standardHeaders: true, // Return rate limit info in the `RateLimit-*` headers
            legacyHeaders: false, // Disable the `X-RateLimit-*` headers
        } // Default/fall-back value
    );
    app.use(rateLimit(rateLimiterConfig));

    // Initialize API
    app.get('/hello', function (req, res) {
        return res.send('Hello World')
    });

    // Initialize server
    app.listen(5000, () => {
        logger.info('Starting server on  port 5000');
    });
};
