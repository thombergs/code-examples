package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.fieldinjection.IceCream;

@SpringBootApplication(scanBasePackages = { "com.example.dependency", "com.example.fieldinjection" })
public class ExampleApplicationFI {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(ExampleApplicationSI.class, args);
		IceCream obj = context.getBean(IceCream.class);
		System.out.println("IceCream : " + obj.toString());
	}

}
