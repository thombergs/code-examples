const chalk = require("chalk");

const defaultMessage = "Hello World";

function logInfo(message) {
  console.log(chalk.blue(message));
}

function logError(message) {
  console.log(chalk.red(message));
}

module.exports = {
  defaultMessage,
  info: logInfo,
  error: logError,
};
