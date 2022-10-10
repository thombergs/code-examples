/**
 * Wrap a promise to invoke an optional callback upon resolution or rejection.
 *
 * This function assumes the callback follows the Node.js callback type: (err, value) => void
 *
 * If a callback is provided:
 *   - if the promise is resolved, invoke the callback with (null, value)
 *   - if the promise is rejected, invoke the callback with (error, null)
 *
 * @param {Promise<any>} promise
 * @param {Function} callback
 * @returns Promise<any> | undefined
 */
module.exports = function wrapPromiseCallback(promise, callback) {
  const ret = promise.then(
    value => {
      if (callback) {
        setImmediate(() => {
          callback(null, value);
        });
      }
      return value;
    },
    error => {
      if (callback) {
        setImmediate(() => {
          callback(error, null);
        });
      } else {
        return Promise.reject(error);
      }
    }
  );

  return !callback ? ret : undefined;
};
