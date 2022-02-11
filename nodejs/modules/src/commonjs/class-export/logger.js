const chalk = require("chalk");

class Logger {
  defaultMessage = "Hello World";

  info(message) {
    console.log(chalk.blue(message));
  }

  error(message) {
    console.log(chalk.red(message));
  }
}

module.exports = new Logger();
