package io.reflectoring.banking_service.kafka.consumer;

import io.reflectoring.banking_service.model.Transaction;
import io.reflectoring.banking_service.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class TransactionConsumer {

    @Bean
    public Consumer<Transaction> consumeTransaction(TransactionService transactionService) {
        return transactionService::asyncProcess;
    }
}
