package io.reflectoring.springbootlogging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringBootLoggingApplication {
    static final Logger logger = LoggerFactory.getLogger(SpringBootLoggingApplication.class);
	
    
    public static void main(String[] args) {
		SpringApplication.run(SpringBootLoggingApplication.class, args);
	  
    }

}
