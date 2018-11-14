const HeroCreatedEvent = require('../common/hero-created-event');

exports.CreateHeroEventProducer = {
    produceHeroCreatedEvent: () => {
        return new Promise((resolve, reject) => {
            resolve(new HeroCreatedEvent("Superman", "Flying", "DC", 42));
        });
    }
};
