# RDS Hello World

This is a simple Spring Boot application which requires access to a PostgreSQL database.

The application has a single endpoint `/hello` which prints out if the database connection was successful.

Get it in a Docker image via `docker pull reflectoring/aws-rds-hello-world`.

Use the image instead of your real application to test AWS CloudFormation stacks which need access to a database.
