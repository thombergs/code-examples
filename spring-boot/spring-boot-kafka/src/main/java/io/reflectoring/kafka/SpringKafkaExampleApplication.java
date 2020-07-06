package io.reflectoring.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SpringKafkaExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaExampleApplication.class, args);
	}
}