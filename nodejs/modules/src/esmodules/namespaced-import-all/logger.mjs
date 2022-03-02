import chalk from "chalk";

export const defaultMessage = "Hello World";

export class Logger {
  static info(message) {
    console.log(chalk.blue(message));
  }

  static error(message) {
    console.log(chalk.red(message));
  }
}
