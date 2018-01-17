# Testing a Spring Boot REST API Consumer against a Contract with Spring Cloud Contract

## Companion Blog Article
Read the [companion blog article](https://reflectoring.io/consumer-driven-contract-consumer-spring-cloud-contract/) to this repository.

## Getting Started

* have a look at the [contract](/src/test/resources/contracts)
* have a look at the [feign client](/src/main/java/io/reflectoring/UserClient.java)
* have a look at the [consumer test](/src/test/java/io/reflectoring/UserClientTest.java)
* run `./gradlew publishToMavenLocal` in the [producer project](../spring-cloud-contract-provider) 
  to create a provider stubs
* run `./gradlew build` in this project to verify the feign client against the stub 