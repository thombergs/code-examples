FROM --platform=linux/amd64 adoptopenjdk/openjdk11:jre-11.0.10_9-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} service.jar
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java -jar -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 service.jar" ]
