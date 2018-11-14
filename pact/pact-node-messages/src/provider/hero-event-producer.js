const HeroCreatedMessage = require('../common/hero-created-message');

function produceHeroCreatedEvent() {
    return new Promise((resolve, reject) => {
        resolve(new HeroCreatedMessage("Superman", "Flying", "DC", 42));
    });
}

module.exports = produceHeroCreatedEvent;