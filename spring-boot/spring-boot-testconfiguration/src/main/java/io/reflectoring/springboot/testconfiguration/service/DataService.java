package io.reflectoring.springboot.testconfiguration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);
    private final WebClient webClient;
    public DataService(final WebClient webClient) {
        this.webClient = webClient;
        LOGGER.info("WebClient instance {}", this.webClient);
    }
}
