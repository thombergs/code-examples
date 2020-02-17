package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.constructorinjection.Cake;

@SpringBootApplication(scanBasePackages= {"com.example.constructorinjection","com.example.dependency"})
public class ExampleApplicationCI  {

	private static Logger LOGGER=LoggerFactory.getLogger(ExampleApplicationCI.class);
	public static void main(String[] args) {
		
		ApplicationContext context = SpringApplication.run(ExampleApplicationCI.class, args);
		Cake obj = context.getBean(Cake.class);
		LOGGER.info("Cake : "+ obj.toString());
	}

}
