import {ApolloClient} from "apollo-client"
import {InMemoryCache} from "apollo-cache-inmemory"
import {HttpLink} from "apollo-link-http"
import gql from "graphql-tag"
import Hero from "../hero";

class GraphQLHeroService {

    constructor(baseUrl, port, fetch) {
        this.client = new ApolloClient({
            link: new HttpLink({
                uri: `${baseUrl}:${port}/graphql`,
                fetch: fetch
            }),
            cache: new InMemoryCache()
        });
    }

    getHero(heroId) {
        if (heroId == null) {
            throw new Error("heroId must not be null!");
        }
        return this.client.query({
            query: gql`
              query GetHero($heroId: Int!) {
                hero(id: $heroId) {
                  name
                  superpower
                }
              }
            `,
            variables: {
                heroId: heroId
            }
        }).then((response) => {
            return new Promise((resolve, reject) => {
                try {
                    const hero = new Hero(response.data.hero.name, response.data.hero.superpower, null, heroId);
                    Hero.validateName(hero);
                    Hero.validateSuperpower(hero);
                    resolve(hero);
                } catch (error) {
                    reject(error);
                }
            })
        });
    };

}

export default GraphQLHeroService;

