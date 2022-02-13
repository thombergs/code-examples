const chalk = require("chalk");

exports.logInfo = function (message) {
  console.log(chalk.blue(message));
};

exports.logError = function logError(message) {
  console.log(chalk.red(message));
};

exports.defaultMessage = "Hello World";
