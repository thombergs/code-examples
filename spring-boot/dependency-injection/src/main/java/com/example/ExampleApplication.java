package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication(scanBasePackages = { "com.example.dependency" })
public class ExampleApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ExampleApplication.class, args);
	}

}
