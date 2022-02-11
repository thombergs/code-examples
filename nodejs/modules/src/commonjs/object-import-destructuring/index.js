const { info, error, defaultMessage } = require("./logger");

info(`${defaultMessage} printed in blue`);
error("some error message printed in red");
