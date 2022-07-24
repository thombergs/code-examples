const { format, createLogger, transports } = require("winston");
const { combine, timestamp, label, printf, prettyPrint } = format;
const CATEGORY = "winston custom format";

const logger = createLogger({
  level: "debug",
  format: combine(
    label({ label: CATEGORY }),
    timestamp({
      format: "MMM-DD-YYYY HH:mm:ss",
    }),
    prettyPrint()
  ),
  transports: [new transports.Console()],
});

module.exports = logger;
