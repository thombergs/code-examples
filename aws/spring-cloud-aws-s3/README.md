## Interacting with Amazon S3 Bucket using Spring Cloud AWS

Codebase demonstrating connection and interaction with provisioned Amazon S3 bucket using [Spring Cloud AWS](https://spring.io/projects/spring-cloud-aws). 

Contains integration tests to validate interaction between the application and Amazon S3 using [LocalStack](https://github.com/localstack/localstack) and [Testcontainers](https://github.com/testcontainers/testcontainers-java). Test cases can be executed with the command `./mvnw integration-test verify`.

To run the application locally without provisioning actual AWS Resources, execute the below commands:

```bash
chmod +x localstack/init-s3-bucket.sh
```

```bash
sudo docker-compose build
```

```bash
sudo docker-compose up -d
```

## Blog posts

Blog posts about this topic:

* [Using Amazon S3 with Spring Cloud AWS](https://reflectoring.io/spring-cloud-aws-s3/)
* [Using AWS S3 Presigned URLs in Spring Boot](https://reflectoring.io/aws-s3-presigned-url-spring-boot/)
