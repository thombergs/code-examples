const chalk = require("chalk");

exports.logInfo = function (message) {
  console.log(chalk.blue(message));
};

exports.defaultMessage = "Hello World";
