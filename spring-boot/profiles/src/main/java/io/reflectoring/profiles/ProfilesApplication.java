package io.reflectoring.profiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProfilesApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(ProfilesApplication.class);
		application.setAdditionalProfiles("baz");
		application.run(args);
	}

}
