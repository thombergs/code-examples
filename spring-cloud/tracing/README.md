# Tracing with Spring Cloud Sleuth, OpenTelemetry and Logz.io
 
- run `./mvnw clean install` to build the two Spring Boot applications (`api-service` and `customer-service`)
- run `LOGZIO_REGION=<YOUR_LOGZIO_REGION> LOGZIO_TRACES_TOKEN=<YOUR_LOGZIO_TRACING_TOKEN> docker-compose up --build`
- call `http://localhost:8080/customers/<ID>` (where ID is a number from 1 to 50)

The above HTTP call goes to the `api-service`, which will call the `customer-service` for additional information. This will create a trace across both services, as should be evident in the logs with the same trace id.

The `docker-compose` command also starts up an OpenTelemetry Collector, to which the Spring Boot apps send their traces. The OpenTelemetry Collector, in turn, sends the traces to Logz.io.

## Companion Article
[Tracing with Spring Boot, OpenTelemetry, and Jaeger](https://reflectoring.io/spring-boot-tracing)
