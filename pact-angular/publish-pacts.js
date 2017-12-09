let projectFolder = __dirname;
let pact = require('@pact-foundation/pact-node');
let project = require('./package.json');

let options = {
  pactUrls: [projectFolder + '/pacts'],
  pactBroker: 'https://adesso.pact.dius.com.au/',
  consumerVersion: project.version,
  tags: ['latest'],
  pactBrokerUsername: 'Vm6YWrQURJ1T7mDIRiKwfexCAc4HbU',
  pactBrokerPassword: 'aLerJwBhpEcN0Wm88Wgvs45AR9dXpc'
};

pact.publishPacts(options).then(function () {
  console.log("Pacts successfully published!");
});
