package io.reflectoring.springboot.testconfiguration.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DataService {
    private final WebClient webClient;
    public DataService(final WebClient webClient) {
        this.webClient = webClient;
        System.out.println("WebClient instance " + this.webClient.toString());
    }
}
