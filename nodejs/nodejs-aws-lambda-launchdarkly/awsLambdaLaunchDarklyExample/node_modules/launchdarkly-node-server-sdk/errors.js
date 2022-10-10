function createCustomError(name) {
  function CustomError(message, code) {
    Error.captureStackTrace && Error.captureStackTrace(this, this.constructor);
    this.message = message;
    this.code = code;
  }

  CustomError.prototype = new Error();
  CustomError.prototype.name = name;
  CustomError.prototype.constructor = CustomError;

  return CustomError;
}

exports.LDPollingError = createCustomError('LaunchDarklyPollingError');
exports.LDStreamingError = createCustomError('LaunchDarklyStreamingError');
exports.LDUnexpectedResponseError = createCustomError('LaunchDarklyUnexpectedResponseError');
exports.LDInvalidSDKKeyError = createCustomError('LaunchDarklyInvalidSDKKeyError');
exports.LDClientError = createCustomError('LaunchDarklyClientError');

exports.isHttpErrorRecoverable = function (status) {
  if (status >= 400 && status < 500) {
    return status === 400 || status === 408 || status === 429;
  }
  return true;
};
