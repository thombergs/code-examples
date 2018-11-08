import Hero from "../hero";
import adapter from 'axios/lib/adapters/http';
const axios = require('axios');

class HeroService {

    constructor(baseUrl, port) {
        this.baseUrl = baseUrl;
        this.port = port;
    }

    getHero(heroId) {
        if (heroId == null) {
            throw new Error("heroId must not be null!");
        }
        return axios.request({
            method: 'GET',
            url: `/heroes/${heroId}`,
            baseURL: `${this.baseUrl}:${this.port}`,
            headers: {
                'Accept': 'application/json; charset=utf-8'
            }
        }, adapter).then((response) => {
            const hero = response.data;
            return new Promise((resolve, reject) => {
                try {
                    this._validateIncomingHero(hero);
                    resolve(hero);
                } catch (error) {
                    reject(error);
                }
            });
        });
    };

    createHero(hero) {
        this._validateHeroForCreation(hero);
        return axios.request({
            method: 'POST',
            url: `/heroes`,
            baseURL: `${this.baseUrl}:${this.port}`,
            headers: {
                'Accept': 'application/json; charset=utf-8',
                'Content-Type': 'application/json; charset=utf-8'
            },
            data: hero
        }, adapter).then((response) => {
            const hero = response.data;
            return new Promise((resolve, reject) => {
                try {
                    this._validateIncomingHero(hero);
                    resolve(hero);
                } catch (error) {
                    reject(error);
                }
            });
        });
    };

    _validateIncomingHero(hero) {
        Hero.validateId(hero);
        Hero.validateName(hero);
        Hero.validateSuperpower(hero);
        Hero.validateUniverse(hero);
    }

    _validateHeroForCreation(hero) {
        delete hero.id;
        Hero.validateName(hero);
        Hero.validateSuperpower(hero);
        Hero.validateUniverse(hero);
    }

}

export default HeroService;

