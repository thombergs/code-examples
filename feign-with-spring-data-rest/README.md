# Handling associations between entities with Spring Data Rest
 
This repo contains some example code that creates a REST client using Feign
that accesses a REST API exposed by Spring Data REST.

## Companion Blog Post

The companion blog post with more details can be found [here](https://reflectoring.io/accessing-spring-data-rest-with-feign/).

## Running the application

In order for this application to run, you need to run the application in the 
[spring-data-rest-associations](https://github.com/thombergs/code-examples/tree/master/spring-data-rest-associations)
example first to provide the REST API for the client in this project.

The, simply run `gradlew bootrun` on Windows or `./gradlew.sh bootrun` on Unix.