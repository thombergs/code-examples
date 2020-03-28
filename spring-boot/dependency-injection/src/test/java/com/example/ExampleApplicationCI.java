package com.example;

import com.example.constructorinjection.Cake;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication(scanBasePackages = { "com.example.constructorinjection", "com.example.dependency" })
public class ExampleApplicationCI {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(ExampleApplicationCI.class, args);
		Cake obj = context.getBean(Cake.class);
		System.out.println("Cake : " + obj.toString());
	}

}
