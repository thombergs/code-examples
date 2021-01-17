package io.reflectoring.cookie.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = { "io.reflectoring.cookie" })
public class CookieDemoApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(CookieDemoApplication.class);
		springApplication.run(args);
	}

}
