# Consumer-Driven-Contract Test for a Feign Consumer
 
This repo contains an example of consumer-driven-contract testing for a Feign client
that consumes a REST API provided by the module `pact-spring-data-rest-provider`.

The contract is created and verified with [Pact](https://docs.pact.io/).

## Companion Blog Post

The Companion Blog Post on [https://reflectoring.io](https://reflectoring.io) will be 
published shortly.

## Running the application

The interesting part in this code base is the class `ConsumerPactVerificationTest`.
You can run the tests with `gradlew test` on Windows or `./gradlew test` on Unix.