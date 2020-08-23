FROM adoptopenjdk:11-jre-hotspot
MAINTAINER pratikdas@yahoo.com
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
