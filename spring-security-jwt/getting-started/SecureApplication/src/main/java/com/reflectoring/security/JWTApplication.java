package com.reflectoring.security;

import com.reflectoring.security.jwt.JWTCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JWTApplication {

	public static void main(String[] args) {
		//System.out.println("Create JWT Token: " + JWTCreator.createJwt());
		//System.out.println("Parse JWT: " + JWTCreator.parseJwt(JWTCreator.createJwt()));
		SpringApplication.run(JWTApplication.class, args);
	}

}
