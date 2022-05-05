package io.reflectoring.account_management_service.kafka.consumer;

import io.reflectoring.account_management_service.model.Transaction;
import io.reflectoring.account_management_service.service.AccountManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class TransactionConsumer {

    @Bean
    public Consumer<Transaction> consumeTransaction(AccountManagementService accountManagementService) {
        return accountManagementService::asyncProcess;
    }
}
