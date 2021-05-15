package io.reflectoring.springboot.testconfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Bean
    public WebClient getWebClient(final WebClient.Builder builder,
                                  @Value("${data.service.endpoint:https://google.com}") final String url) {
        WebClient webClient = builder.baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                // more configurations and customizations
                .build();
        System.out.println("WebClient Instance Created During Testing: " + webClient.toString());
        return webClient;
    }
}
