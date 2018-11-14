const HeroCreatedEvent = require('../common/hero-created-event');

exports.HeroEventHandler = {
    handleHeroCreatedEvent: (message) => {

        HeroCreatedEvent.validateId(message);
        HeroCreatedEvent.validateName(message);
        HeroCreatedEvent.validateSuperpower(message);
        HeroCreatedEvent.validateUniverse(message);

        // pass message into business logic
        // note that the business logic should be mocked away for the contract test

    }
};
