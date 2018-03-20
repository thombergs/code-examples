# Testing a Spring Boot REST API Consumer against a Contract with Pact

## Companion Blog Article
Read the [companion blog article](http://localhost:4000/consumer-driven-contract-feign-pact/) to this repository.

## Getting Started

* have a look at the [feign client](src/main/java/io/reflectoring/UserClient.java)
* have a look at the [consumer test](src/test/java/io/reflectoring/UserServiceConsumerTest.java)
* run `./gradlew build` in this project to create a pact and run the consumer test
* afterwards, find the pact contract file in the folder `target/pacts` 
