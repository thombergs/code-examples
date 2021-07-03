# Working with AWS DynamoDB and Spring

Example code for using Spring AWS DynamoDB

## Blog posts

Blog posts about this topic:

* [Working with AWS DynamoDB and Spring ](https://reflectoring.io/spring-dynamodb/)

This is a nested Maven project composed of two modules:

1. dynamodbec: Contains a Spring Boot project which uses Enhanced DynamoDB Client to make database operations on DynamoDB.

2. dynamodbspringdata: Contains a Spring Boot project which uses Spring Data to perform operations on DynamoDB.

In both projects, the Spring Boot dependency is added in the `dependencyManagement` block in `pom.xml`
.


