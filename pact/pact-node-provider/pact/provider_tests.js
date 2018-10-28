const { Verifier } = require('@pact-foundation/pact');
const packageJson = require('../package.json');

let opts = {
    providerBaseUrl: 'http://localhost:3000',
    provider: 'hero-provider',
    pactBrokerUrl: 'https://adesso.pact.dius.com.au',
    pactBrokerUsername: process.env.PACT_USERNAME,
    pactBrokerPassword: process.env.PACT_PASSWORD,
    publishVerificationResult: true,
    providerVersion: packageJson.version,
    providerStatesSetupUrl: 'http://localhost:3000/provider-state'
};

new Verifier().verifyProvider(opts).then(function () {
    console.log("Pacts successfully verified!");
});