package io.reflectoring.springboot.testconfiguration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class WebClientTestConfiguration {
    @Bean
    public WebClient getWebClient(final WebClient.Builder builder) {
        WebClient webClient = builder
                .baseUrl("http://localhost")
                .build();
        System.out.println("WebClient Instance Created During Testing: " + webClient.toString());
        return webClient;
    }
}
