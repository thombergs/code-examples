package io.reflectoring.cache.cleint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HazelcastClientServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastClientServerApplication.class, args);
    }

}
