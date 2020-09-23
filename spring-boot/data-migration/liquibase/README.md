# liquibase-demo

This project demonstrates on how to set-up liquibase in a Spring Boot application.

When this project is run, it executes the changelogs and creates a `user_details` table and populates it with some test data.
It also creates an index and couple of constraints on `user_details` table.

The project also demonstrates the following.

* Organising liquibase change logs [using master changelog](src/main/resources/db/changelog/db.changelog-master.yaml)
* It demonstrates how to write a [change log in YAML format](src/main/resources/db/changelog/db.changelog-yaml-example.yaml)
* It demonstrates how to write a [change log in JSON format](src/main/resources/db/changelog/db.changelog-json-example.json)
* It demonstrates how to write a [change log in XML format](src/main/resources/db/changelog/db.changelog-xml-example.xml)
* It demonstrates how to write a [change log in SQL format](src/main/resources/db/changelog/db.changelog-sql-example.sql)
* Enable `INFO` [logs for liquibase](src/main/resources/application.yaml#L10-L12). 
* It demonstrates testing of changes done by liquibase to the [schema using Junit test](src/test/java/io/reflectoring/liquibase/adapter/datastore/UserRepositoryDockerProfileTest.java)
* It demonstrates use of liquibase [change log parameters](src/main/resources/db/changelog/db.changelog-yaml-example.yaml#L21-L33)
* IT demonstrates use of liquibase [context](src/main/resources/db/changelog/db.changelog-xml-example.xml#L5)

## Related Blog Posts

* [One-Stop Guide to Database Migration with Liquibase and Spring Boot](https://reflectoring.io/database-migration-spring-boot-liquibase/)
* [Tool-based Database Refactoring: Flyway vs. Liquibase](https://reflectoring.io/database-refactoring-flyway-vs-liquibase/)

## How to Run the project and tests

### Have Docker and Docker Compose in your machine?

#### Run application
- Clone this repository 

```
 git clone https://github.com/thombergs/code-examples/spring-boot/data-migration/liquibase
```
- Move to the directory `code-examples/spring-boot/data-migration/liquibase`

- Run the docker compose file `infra-local.yaml` in `src/docker`. This starts the postgres database needed to run the application.
```
   docker-compose -f ./src/main/docker/infra-local.yaml up -d
```
- Now run the spring boot application as follows.
```
    mvnw spring-boot:run -Dspring-boot.run.profiles=docker
```

The application should start and listening on port `8080`.

Open the url (http://localhost:8080/liquibase/users/100000000) in the browser. You should see a response as below

```json
{
"id": 100000000,
"userName": "testUser",
"firstName": "testFirstName",
"lastName": "testLastName"
}
```

You can login to the database and check the creation of `user_details` table along with some test data in it. 
```yaml
    #Use below credentials to login to the database
    databaseHost: localhost
    jdbcUrl: jdbc:postgresql://localhost:5432/liquibasedemo?current_schema=public
    port: 5432
    username: demouser
    password: demopassword
```

Also, have a look at databasechangelog and databasechangeloglock tables.

#### Run Test
To run integration test, you don't need to run the docker compose file. Just run the test as follows:

```yaml
mvn test -Dspring.profiles.active=docker
 
```
The test uses [TestContainers](https://www.testcontainers.org/) to spin a postgres database, which is used during the integration test.

### Don't have docker?

You can use h2 profile to run the app and use h2 database.

- Clone this repository 

```
 git clone https://github.com/thombergs/code-examples/spring-boot/data-migration/liquibase
```
- Move to the directory `code-examples/spring-boot/data-migration/liquibase`

- Now run the spring boot application as follows.
```
    mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```

The application should start and listening on port `8080`.

Open the url (http://localhost:8080/liquibase/users/100000000) in the browser. You should see a response as below

```json
{
"id": 100000000,
"userName": "testUser",
"firstName": "testFirstName",
"lastName": "testLastName"
}
```

Login to h2 database console using [url](http://localhost:8080/liquibase/h2-console)

```yaml
    JDBC URL: jdbc:h2:mem:liquibasedemo
    username: demouser
    password: demopassword
```
Check the creation of `user_details` table along with some test data in it.

Also, have a look at databasechangelog and databasechangeloglock tables.

#### Run Test
To run integration test

```yaml
mvn test -Dspring.profiles.active=h2
 
```

