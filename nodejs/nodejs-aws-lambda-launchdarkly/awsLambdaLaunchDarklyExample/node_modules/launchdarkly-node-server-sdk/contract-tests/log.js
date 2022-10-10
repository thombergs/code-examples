const ld = require('launchdarkly-node-server-sdk');

function Log(tag) {
  function doLog(level, message) {
    console.log(new Date().toISOString() + ` [${tag}] ${level}: ${message}`);
  }
  return {
    info: message => doLog('info', message),
    error: message => doLog('error', message),
  };
}

function sdkLogger(tag) {
  return ld.basicLogger({
    level: 'debug',
    destination: line => {
      console.log(new Date().toISOString() + ` [${tag}.sdk] ${line}`);
    },
  });
}

module.exports.Log = Log;
module.exports.sdkLogger = sdkLogger;
