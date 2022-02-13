const chalk = require("chalk");

function info(message) {
  console.log(chalk.blue(message));
}

function error(message) {
  console.log(chalk.red(message));
}

const defaultMessage = "Hello World";

module.exports = {
  logInfo: info,
  logError: error,
  defaultMessage,
};
