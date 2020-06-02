package io.refectoring.cache.embedded;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HazelcastEmbeddedCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastEmbeddedCacheApplication.class, args);
    }

}
