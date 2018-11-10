import GraphQLHeroService from './hero.service.graphql';
import * as Pact from '@pact-foundation/pact';
import fetch from 'node-fetch';

describe('HeroService GraphQL API', () => {

    const heroService = new GraphQLHeroService('http://localhost', global.port, fetch);

    // a matcher for the content type "application/json" in UTF8 charset
    // that ignores the spaces between the ";2 and "charset"
    const contentTypeJsonMatcher = Pact.Matchers.term({
        matcher: "application\\/json; *charset=utf-8",
        generate: "application/json; charset=utf-8"
    });

    describe('getHero()', () => {

        beforeEach((done) => {

            global.provider.addInteraction(new Pact.GraphQLInteraction()
                .uponReceiving('a GetHero Query')
                .withRequest({
                    path: '/graphql',
                    method: 'POST',
                })
                .withOperation("GetHero")
                .withQuery(`
                    query GetHero($heroId: Int!) {
                      hero(id: $heroId) {
                          name
                          superpower
                          __typename
                      }
                    }`)
                .withVariables({
                    heroId: 42
                })
                .willRespondWith({
                    status: 200,
                    headers: {
                        'Content-Type': contentTypeJsonMatcher
                    },
                    body: {
                        data: {
                            hero: {
                                name: Pact.Matchers.somethingLike('Superman'),
                                superpower: Pact.Matchers.somethingLike('Flying'),
                                __typename: 'Hero'
                            }
                        }
                    }
                })).then(() => done());
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

});
