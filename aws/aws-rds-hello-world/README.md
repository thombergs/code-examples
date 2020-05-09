# RDS Hello World Application

This is a simple Spring Boot application which requires access to a PostgreSQL database.

The application has a single endpoint `/hello` which prints out if the database connection was successful.

Get it in a Docker image via `docker pull reflectoring/aws-rds-hello-world`.

Use the image instead of your real application to test AWS CloudFormation stacks which need access to a database.

## Testing AWS RDS connectivity with this application

1. Create an RDS PostgreSQL database with the AWS console.
2. Note the endpoint of your RDS database in the AWS console.
3. Deploy the Docker container `reflectoring/aws-rds-hello-world` into AWS instead of your real application (this could be via a CloudFormation stack, manually, or however you are deploying your app).
4. Configure your deployment in a way that Docker will pass the coordinates to your RDS database as environment variables, equivalent to this command:
    ``` 
    docker run \
      -e SPRING_DATASOURCE_URL=jdbc:postgresql://<RDS-ENDPOINT>:5432/postgres \
      -e SPRING_DATASOURCE_USERNAME=<USERNAME> \
      -e SPRING_DATASOURCE_PASSWORD=<PASSWORD> \
      -p 8080:8080 reflectoring/aws-rds-hello-world
    ```
5. If the Spring Boot application can connect to the database, it will start up sucessfully and serve a message on the endpoint `/hello`.

