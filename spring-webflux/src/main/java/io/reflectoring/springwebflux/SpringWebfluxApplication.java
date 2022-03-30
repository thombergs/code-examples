package io.reflectoring.springwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableMongoAuditing
@EnableReactiveMongoRepositories
@SpringBootApplication
public class SpringWebfluxApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxApplication.class, args);
	}
}
