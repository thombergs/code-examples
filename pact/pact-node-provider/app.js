const createError = require('http-errors');
const express = require('express');
const path = require('path');
const logger = require('morgan');
const heroesRouter = require('./routes/heroes');
const providerStateRouter = require('./routes/provider_state');
const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));

app.use('/heroes', heroesRouter);

registerPactEndpoint();

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    next(createError(404));
});

module.exports = app;

/**
 * Registers a special endpoint for pact provider tests that allows to
 * set the server into a certain "provider state".
 *
 * This endpoint is only registered if the environment variable "PACT_MODE"
 * is set to "true".
 */
function registerPactEndpoint() {
    const pactMode = (process.env.PACT_MODE === 'true');
    if (pactMode) {
        app.use('/provider-state', providerStateRouter);
    }
}
