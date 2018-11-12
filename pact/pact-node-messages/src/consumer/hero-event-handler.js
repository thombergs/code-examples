const HeroCreatedMessage = require('../common/hero-created-message');

function handleHeroCreatedEvent(message) {

    HeroCreatedMessage.validateId(message);
    HeroCreatedMessage.validateName(message);
    HeroCreatedMessage.validateSuperpower(message);
    HeroCreatedMessage.validateUniverse(message);

    // pass message into business logic
    // note that the business logic should be mocked away for the contract test

}

module.exports =  handleHeroCreatedEvent;
