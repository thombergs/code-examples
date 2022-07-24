const { createLogger, transports, format } = require("winston");

const logger = createLogger({
  level: "debug",
  format: format.json(),
  //logger method...
  transports: [
    //new transports:
    new transports.File({
      filename: "logs/example.log",
    }),
  ],
  //...
});

module.exports = logger;
