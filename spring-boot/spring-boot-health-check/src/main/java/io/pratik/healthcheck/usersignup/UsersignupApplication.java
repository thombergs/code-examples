package io.pratik.healthcheck.usersignup;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class UsersignupApplication {

	@Autowired
	private UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(UsersignupApplication.class, args);
	}
	@Bean
	InitializingBean populateUsers() {
		return () -> {
			userRepository.deleteAll();
			userRepository.save(User.builder().firstName("Roger").lastName("Federer").profileURL("http://linkin.com/576567").build());
			userRepository.save(User.builder().firstName("Rafal").lastName("Nadal").build());
			userRepository.save(User.builder().firstName("Novak").lastName("Djokovik").build());
		};
	}
}
