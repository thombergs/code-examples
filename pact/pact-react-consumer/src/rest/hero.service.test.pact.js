import HeroService from './hero.service';
import * as Pact from '@pact-foundation/pact';
import Hero from '../hero';

describe('HeroService API', () => {

    const heroService = new HeroService('http://localhost', global.port);

    // a matcher for the content type "application/json" in UTF8 charset
    // that ignores the spaces between the ";2 and "charset"
    const contentTypeJsonMatcher = Pact.Matchers.term({
        matcher: "application\\/json; *charset=utf-8",
        generate: "application/json; charset=utf-8"
    });

    describe('getHero()', () => {

        beforeEach((done) => {
            global.provider.addInteraction({
                state: 'a hero exists',
                uponReceiving: 'a GET request for a hero',
                withRequest: {
                    method: 'GET',
                    path: '/heroes/42',
                    headers: {
                        'Accept': contentTypeJsonMatcher
                    }
                },
                willRespondWith: {
                    status: 200,
                    headers: {
                        'Content-Type': contentTypeJsonMatcher
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
                        'Accept': contentTypeJsonMatcher,
                        'Content-Type': contentTypeJsonMatcher
                    },
                    body: new Hero('Superman', 'flying', 'DC')
                },
                willRespondWith: {
                    status: 201,
                    headers: {
                        'Content-Type': Pact.Matchers.term({
                            matcher: "application\\/json; *charset=utf-8",
                            generate: "application/json; charset=utf-8"
                        })
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
