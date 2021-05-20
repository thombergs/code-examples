package io.reflectoring.springboot.testconfiguration;

import io.reflectoring.springboot.testconfiguration.service.DataService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class UsingStaticInnerTestConfiguration {
    @Autowired
    private DataService dataService;

    @Test
    void contextLoads() {
    }

    @TestConfiguration
    public static class WebClientTestConfiguration {
        private static final Logger LOGGER = LoggerFactory.getLogger(WebClientTestConfiguration.class);
        @Bean
        public WebClient getWebClient(final WebClient.Builder builder) {
            WebClient webClient = builder
                    .baseUrl("http://localhost")
                    .build();
            LOGGER.info("WebClient Instance Created During Testing, using static inner class: {}", webClient);
            return webClient;
        }
    }
}
