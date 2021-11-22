# Log Tracing with Spring Cloud Sleuth
 
This project shows how to implement tracing in a network of Spring Boot applications.

## Companion Blog Article
The companion blog article to this repository can be found [here](https://reflectoring.io/tracing-with-spring-cloud-sleuth/).

## Getting Started

* build the sources with `./gradlew clean build` (don't forge to also do this after every change to the code, otherwise Docker will start the old image)
* run the applications in Docker with `docker-compose up --build`