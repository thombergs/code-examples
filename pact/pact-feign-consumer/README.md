# Creating a Consumer-Driven Contract with Feign and Pact

## Companion Blog Article
Read the [companion blog article](https://reflectoring.io/consumer-driven-contract-feign-pact/) to this repository.

## Getting Started

* have a look at the [feign client](/src/main/java/io/reflectoring/UserClient.java)
* have a look at the [consumer test](/src/test/java/io/reflectoring/UserServiceConsumerTest.java)
* run `./gradlew build` to run all tests and create pact files into the folder `target/pacts`
* run `./gradlew pactPublish` to publish the pact files to a Pact Broker (must specify Pact Broker location and credentials in `build.gradle`)