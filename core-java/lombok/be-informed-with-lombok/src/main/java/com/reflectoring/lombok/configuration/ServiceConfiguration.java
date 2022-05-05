package com.reflectoring.lombok.configuration;

import com.reflectoring.lombok.processor.DataProcessor;
import com.reflectoring.lombok.processor.FileDataProcessor;
import com.reflectoring.lombok.service.SneakyThrowsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public SneakyThrowsService sneakyThrowsService(DataProcessor dataProcessor) {
        return new SneakyThrowsService(dataProcessor);
    }

    @Bean
    public DataProcessor dataProcessor() {
        return new FileDataProcessor();
    }
}
