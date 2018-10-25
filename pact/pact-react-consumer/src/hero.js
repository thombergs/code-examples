class Hero {
    constructor(name, superpower, universe, id) {
        this.id = id;
        this.name = name;
        this.superpower = superpower;
        this.universe = universe;
    }

    static validateUniverse(hero) {
        if (typeof hero.universe !== 'string') {
            throw new Error(`Hero universe must be a string! Invalid value: ${hero.universe}`)
        }
    }

    static validateSuperpower(hero) {
        if (typeof hero.superpower !== 'string') {
            throw new Error(`Hero superpower must be a string! Invalid value: ${hero.superpower}`)
        }
    }

    static validateName(hero) {
        if (typeof hero.name !== 'string') {
            throw new Error(`Hero name must be a string! Invalid value: ${hero.name}`);
        }
    }

    static validateId(hero) {
        if (typeof hero.id !== 'number') {
            throw new Error(`Hero id must be a number! Invalid value: ${hero.id}`)
        }
    }
}

export default Hero;