class HeroCreatedEvent {

    constructor(name, superpower, universe, id) {
        this.id = id;
        this.name = name;
        this.superpower = superpower;
        this.universe = universe;
    }

    static validateUniverse(event) {
        if (typeof event.universe !== 'string') {
            throw new Error(`Hero universe must be a string! Invalid value: ${event.universe}`)
        }
    }

    static validateSuperpower(event) {
        if (typeof event.superpower !== 'string') {
            throw new Error(`Hero superpower must be a string! Invalid value: ${event.superpower}`)
        }
    }

    static validateName(event) {
        if (typeof event.name !== 'string') {
            throw new Error(`Hero name must be a string! Invalid value: ${event.name}`);
        }
    }

    static validateId(event) {
        if (typeof event.id !== 'number') {
            throw new Error(`Hero id must be a number! Invalid value: ${event.id}`)
        }
    }
}

module.exports = HeroCreatedEvent;