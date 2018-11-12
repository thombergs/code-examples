const HeroCreatedMessage = require('../common/hero-created-message');

function produceHeroCreatedEvent(id) {
    return new Promise((resolve, reject) => {
        resolve({foo: "this is an invalid message that does not match the contract!"});
    });
}

module.exports = produceHeroCreatedEvent;