let projectFolder = __dirname;
let pact = require('@pact-foundation/pact-node');
let project = require('./package.json');
let pactBrokerUrl = process.env.npm_package_config_brokerUrl;
let pactBrokerUsername = process.env.npm_package_config_brokerUsername;
let pactBrokerPassword = process.env.npm_package_config_brokerPassword;

let options = {
  pactFilesOrDirs: [projectFolder + '/pacts'],
  pactBroker: pactBrokerUrl,
  consumerVersion: project.version,
  tags: ['latest'],
  pactBrokerUsername: pactBrokerUsername,
  pactBrokerPassword: pactBrokerPassword
};


pact.publishPacts(options).then(function () {
  console.log("Pacts successfully published!");
});
