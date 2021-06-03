package io.reflectoring.springboot.testconfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class WebClientTestConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsingStaticInnerTestConfiguration.WebClientTestConfiguration.class);
    @Bean
    public WebClient getWebClient(final WebClient.Builder builder) {
        WebClient webClient = builder
                .baseUrl("http://localhost")
                .build();
        LOGGER.info("WebClient Instance Created During Testing: {}", webClient);
        return webClient;
    }
}
