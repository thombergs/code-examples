const {MessageProviderPact} = require('@pact-foundation/pact');
const {CreateHeroEventProducer} = require('./hero-event-producer');
const path = require('path');

describe("message producer", () => {

    const messagePact = new MessageProviderPact({
        messageProviders: {
            "a hero created message": () => CreateHeroEventProducer.produceHeroCreatedEvent(),
        },
        log: path.resolve(process.cwd(), "logs", "pact.log"),
        logLevel: "info",
        provider: "node-message-provider",

        pactUrls: [path.resolve(process.cwd(), "pacts", "node-message-consumer-node-message-provider.json")],

        // Pact seems not to load a pact file from a pact broker, so we have to make do with the local pact file
        // see https://github.com/pact-foundation/pact-js/issues/248
        // pactBrokerUrl: "https://adesso.pact.dius.com.au",
        // pactBrokerUsername: process.env.PACT_USERNAME,
        // pactBrokerPassword: process.env.PACT_PASSWORD,
        // publishVerificationResult: true,
        // providerVersion: '1.0.0',
        tags: ['latest']
    });

    describe("'hero created' message producer", () => {

        it("should create a valid hero created message", (done) => {
            messagePact
                .verify()
                .then(() => done(), (error) => done(error));
        }).timeout(5000);

    });

});