package io.reflectoring.user_notification_service.kafka.consumer;

import io.reflectoring.user_notification_service.model.Transaction;
import io.reflectoring.user_notification_service.service.UserNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class TransactionConsumer {

    @Bean
    public Consumer<Transaction> consumeTransaction(UserNotificationService userNotificationService) {
        return userNotificationService::asyncProcess;
    }
}
