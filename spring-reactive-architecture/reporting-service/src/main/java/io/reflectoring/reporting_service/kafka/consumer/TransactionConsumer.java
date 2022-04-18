package io.reflectoring.reporting_service.kafka.consumer;

import io.reflectoring.reporting_service.model.Transaction;
import io.reflectoring.reporting_service.service.ReportingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class TransactionConsumer {

    @Bean
    public Consumer<Transaction> consumeTransaction(ReportingService reportingService) {
        return reportingService::asyncProcess;
    }
}
