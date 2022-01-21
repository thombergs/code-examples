package io.reflectoring.featureflags;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FeatureFlagsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeatureFlagsApplication.class, args);
	}

}
