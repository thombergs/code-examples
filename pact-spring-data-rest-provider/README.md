# Consumer-Driven-Contract Test for a Spring Data Rest Provider
 
This repo contains an example of consumer-driven-contract testing for a Spring
Data REST API provider. The corresponding consumer to the contract is
implemented in the module `pact-feign-consumer`.

The contract is created and verified with [Pact](https://docs.pact.io/).

## Companion Blog Post

The Companion Blog Post on [https://reflectoring.io](https://reflectoring.io) will be 
published shortly.

## Running the application

The interesting part in this code base is the class `ProviderPactVerificationTest`.
You can run the tests with `gradlew test` on Windows or `./gradlew test` on Unix.