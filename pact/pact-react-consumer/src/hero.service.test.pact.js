import HeroService from './hero.service';
import * as Pact from '@pact-foundation/pact';
import Hero from './hero';

describe('HeroService API', () => {

    const heroService = new HeroService('http://localhost', global.port);

    describe('getHero()', () => {

        beforeEach((done) => {
            global.provider.addInteraction({
                state: 'a hero exists',
                uponReceiving: 'a GET request for a hero',
                withRequest: {
                    method: 'GET',
                    path: '/heroes/42',
                    headers: {
                        'Accept': 'application/json'
                    }
                },
                willRespondWith: {
                    status: 200,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: Pact.Matchers.somethingLike(new Hero('Superman', 'flying', 'DC', 42))
                }
            }).then(() => done());
        });

        it('sends a request according to contract', (done) => {
            heroService.getHero(42)
                .then(hero => {
                    expect(hero.name).toEqual('Superman');
                })
                .then(() => {
                    global.provider.verify()
                        .then(() => done(), error => {
                            done.fail(error)
                        })
                });
        });

    });

    describe('createHero()', () => {

        beforeEach((done) => {
            global.provider.addInteraction({
                state: 'provider allows hero creation',
                uponReceiving: 'a POST request to create a hero',
                withRequest: {
                    method: 'POST',
                    path: '/heroes',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: new Hero('Superman', 'flying', 'DC')
                },
                willRespondWith: {
                    status: 201,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: Pact.Matchers.somethingLike(
                        new Hero('Superman', 'flying', 'DC', 42))
                }
            }).then(() => done());
        });

        it('sends a request according to contract', (done) => {
            heroService.createHero(new Hero('Superman', 'flying', 'DC'))
                .then(hero => {
                    expect(hero.id).toEqual(42);
                })
                .then(() => {
                    global.provider.verify()
                        .then(() => done(), error => {
                            done.fail(error)
                        })
                });
        });
    });

});
