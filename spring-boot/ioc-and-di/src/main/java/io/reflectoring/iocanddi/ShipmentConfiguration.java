package io.reflectoring.iocanddi;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;

@Configuration
public class ShipmentConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate =
                new RestTemplateBuilder()
                        .setConnectTimeout(Duration.ofMillis(1000))
                        .setReadTimeout(Duration.ofMillis(2000))
                        .build();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://payment-service-uri:8080"));

        return restTemplate;
    }

}
