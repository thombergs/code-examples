class HeroCreatedMessage {

    constructor(name, superpower, universe, id) {
        this.id = id;
        this.name = name;
        this.superpower = superpower;
        this.universe = universe;
    }

    static validateUniverse(message) {
        if (typeof message.universe !== 'string') {
            throw new Error(`Hero universe must be a string! Invalid value: ${message.universe}`)
        }
    }

    static validateSuperpower(message) {
        if (typeof message.superpower !== 'string') {
            throw new Error(`Hero superpower must be a string! Invalid value: ${message.superpower}`)
        }
    }

    static validateName(message) {
        if (typeof message.name !== 'string') {
            throw new Error(`Hero name must be a string! Invalid value: ${message.name}`);
        }
    }

    static validateId(message) {
        if (typeof message.id !== 'number') {
            throw new Error(`Hero id must be a number! Invalid value: ${message.id}`)
        }
    }
}

module.exports = HeroCreatedMessage;