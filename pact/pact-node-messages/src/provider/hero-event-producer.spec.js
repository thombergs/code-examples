const {MessageProviderPact} = require('@pact-foundation/pact');
const produceHeroCreatedEvent = require('./hero-event-producer');
const path = require('path');

describe("message producer", () => {

    const messagePact = new MessageProviderPact({
        messageProviders: {
            "a hero created message": () => produceHeroCreatedEvent(42),
        },
        log: path.resolve(process.cwd(), "logs"),
        logLevel: "debug",
        provider: "node-message-provider",

        pactUrls: [path.resolve(process.cwd(), "pacts", "node-message-consumer-node-message-provider.json")],

        // pactBrokerUrl: "https://adesso.pact.dius.com.au",
        // pactBrokerUsername: process.env.PACT_USERNAME,
        // pactBrokerPassword: process.env.PACT_PASSWORD,
        publishVerificationResult: true,
        providerVersion: '1.0.0',
        tags: ['latest']
    });


    describe("'hero created' message producer", () => {

        it("should create a valid hero created message", (done) => {
            messagePact
                .verify()
                .then(done, (error) => done(error));
        }).timeout(5000);

    });

});