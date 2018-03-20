# Testing a Spring Boot REST API against a Contract with Spring Cloud Contract

## Companion Blog Article
Read the [companion blog article](https://reflectoring.io/consumer-driven-contract-provider-spring-cloud-contract/) to this repository.

## Getting Started

* have a look at the [contract](src/test/resources/contracts/userservice)
* have a look at the [controller](src/main/java/io/reflectoring/UserController.java)
* run `./gradlew generateContractTests` to generate JUnit tests that validate the controller against the contract
* run `./gradlew build` to generate and run the tests
