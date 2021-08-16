package io.reflectoring.structuredlogging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StructuredLoggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StructuredLoggingApplication.class, args);
	}

}
