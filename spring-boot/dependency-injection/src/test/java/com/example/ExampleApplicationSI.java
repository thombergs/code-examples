package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.setterinjection.Cookie;

@SpringBootApplication(scanBasePackages = { "com.example.setterinjection", "com.example.dependency", })
public class ExampleApplicationSI {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(ExampleApplicationSI.class, args);
		Cookie obj = context.getBean(Cookie.class);
		System.out.println("Cookie : " + obj.toString());
	}

}
