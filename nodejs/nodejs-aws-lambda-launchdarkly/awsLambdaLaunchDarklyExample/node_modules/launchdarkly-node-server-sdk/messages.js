const errors = require('./errors');

exports.deprecated = (oldName, newName) => `"${oldName}" is deprecated, please use "${newName}"`;

exports.httpErrorMessage = (err, context, retryMessage) => {
  let desc;
  if (err.status) {
    desc = `error ${err.status}${err.status === 401 ? ' (invalid SDK key)' : ''}`;
  } else {
    desc = `I/O error (${err.message || err})`;
  }
  const action = errors.isHttpErrorRecoverable(err.status) ? retryMessage : 'giving up permanently';
  return `Received ${desc} for ${context} - ${action}`;
};

exports.missingUserKeyNoEvent = () => 'User was unspecified or had no key; event will not be sent';

exports.optionBelowMinimum = (name, value, min) =>
  `Config option "${name}" had invalid value of ${value}, using minimum of ${min} instead`;

exports.unknownOption = name => `Ignoring unknown config option "${name}"`;

exports.wrongOptionType = (name, expectedType, actualType) =>
  `Config option "${name}" should be of type ${expectedType}, got ${actualType}, using default value`;

exports.wrongOptionTypeBoolean = (name, actualType) =>
  `Config option "${name}" should be a boolean, got ${actualType}, converting to boolean`;

exports.invalidTagValue = name => `Config option "${name}" must only contain letters, numbers, ., _ or -.`;

exports.tagValueTooLong = name => `Value of "${name}" was longer than 64 characters and was discarded.`;
