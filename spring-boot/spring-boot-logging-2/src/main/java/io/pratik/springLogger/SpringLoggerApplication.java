package io.pratik.springLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringLoggerApplication {
    static final Logger logger = LoggerFactory.getLogger(SpringLoggerApplication.class);
	
 
    public static void main(String[] args) {
		logger.info("Before Starting application");
		SpringApplication.run(SpringLoggerApplication.class, args);
		logger.debug("Starting my application in debug with {} arguments", args.length);
		logger.info("Starting my application with {} arguments.", args.length);
	  
    }

}
