# Consumer-Driven-Contract Test for a Spring Boot Provider
 
This repo contains an example of consumer-driven-contract testing for a Spring
Boot API provider. The corresponding consumer to the contract is
implemented in the module `pact-angular`.

The contract is created and verified with [Pact](https://docs.pact.io/).

Before running the build, you need to follow the instructions on the [consumer-side](../pact-angular/)
to create the consumer-driven contract file (pact file).

## Running the application

The interesting part in this code base is the class `UserControllerProviderTest`.
You can run the tests with `gradlew test` on Windows or `./gradlew test` on Unix.