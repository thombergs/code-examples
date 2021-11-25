# Tracing with Spring Cloud Sleuth, OpenTelemetry and Logz.io
 
- run `./mvnw clean install` to build the two Spring Boot applications (`downstream-service` and `upstream-service`)
- run `LOGZIO_REGION=<YOUR_LOGZIO_REGION> LOGZIO_TRACES_TOKEN=<YOUR_LOGZIO_TRACING_TOKEN> docker-compose up --build`
- call `http://localhost:8080/customers-with-address/<ID>` (where ID is a number from 1 to 50)

The above HTTP call goes to the `downstream-service`, which will call the `upstream-service` for additional information. This will create a trace across both services, as should be evident in the logs with the same trace id.

The `docker-compose` command also starts up an OpenTelemetry Collector, to which the Spring Boot apps send their traces. The OpenTelemetry Collector, in turn, sends the traces to Logz.io.