const {MessageConsumerPact, Matchers, synchronousBodyHandler} = require('@pact-foundation/pact');
const {HeroEventHandler} = require('./hero-event-handler');
const path = require('path');

describe("message consumer", () => {

    const messagePact = new MessageConsumerPact({
        consumer: "node-message-consumer",
        provider: "node-message-provider",
        dir: path.resolve(process.cwd(), "pacts"),
        pactfileWriteMode: "update",
        logLevel: "info",
    });

    describe("'hero created' message Handler", () => {

        it("should accept a valid hero created message", (done) => {
            messagePact
                .expectsToReceive("a hero created message")
                .withContent({
                    id: Matchers.like(42),
                    name: Matchers.like("Superman"),
                    superpower: Matchers.like("flying"),
                    universe: Matchers.term({generate: "DC", matcher: "^(DC|Marvel)$"})
                })
                .withMetadata({
                    "content-type": "application/json",
                })
                .verify(synchronousBodyHandler(HeroEventHandler.handleHeroCreatedEvent))
                .then(() => done(), (error) => done(error));
        }).timeout(5000);

    });

});