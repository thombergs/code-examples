const httpUtils = require('./utils/httpUtils');

/**
 * Creates a new Requestor object, which handles remote requests to fetch feature flags or segments for LaunchDarkly.
 * This is never called synchronously when requesting a feature flag for a user (e.g. via the variation method).
 *
 * It will be called at the configured polling interval in polling mode. Older versions of the SDK also
 * could use the Requestor to make a polling request even in streaming mode, for very large data sets,
 * but the LD infrastructure no longer uses that behavior.
 *
 * @param {String} the SDK key
 * @param {Object} the LaunchDarkly client configuration object
 **/
function Requestor(sdkKey, config) {
  const requestor = {};

  const headers = httpUtils.getDefaultHeaders(sdkKey, config);
  const requestWithETagCaching = httpUtils.httpWithETagCache();

  function makeRequest(resource) {
    const url = config.baseUri + resource;
    const requestParams = { method: 'GET', headers };
    return (cb, errCb) => {
      requestWithETagCaching(url, requestParams, null, config, (err, resp, body) => {
        if (err) {
          errCb(err);
        } else {
          cb(resp, body);
        }
      });
    };
  }

  function processResponse(cb) {
    return (response, body) => {
      if (response.statusCode !== 200 && response.statusCode !== 304) {
        const err = new Error('Unexpected status code: ' + response.statusCode);
        err.status = response.statusCode;
        cb(err, null);
      } else {
        cb(null, response.statusCode === 304 ? null : body);
      }
    };
  }

  function processErrorResponse(cb) {
    return err => {
      cb(err, null);
    };
  }

  // Note that requestAllData will pass (null, null) rather than (null, body) if it gets a 304 response;
  // this is deliberate so that we don't keep updating the data store unnecessarily if there are no changes.
  requestor.requestAllData = cb => {
    const req = makeRequest('/sdk/latest-all');
    req(processResponse(cb), processErrorResponse(cb));
  };

  return requestor;
}

module.exports = Requestor;
