package io.reflectoring.staticdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class StaticApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaticApplication.class, args);
	}

}
