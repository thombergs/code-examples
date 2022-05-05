package io.reflectoring.user_notification_service.controller;

import io.reflectoring.user_notification_service.model.Transaction;
import io.reflectoring.user_notification_service.service.UserNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/notify")
public class UserNotificationController {

    @Autowired
    private UserNotificationService userNotificationService;

    @PostMapping("/fraudulent-transaction")
    public Mono<Transaction> notify(@RequestBody Transaction transaction) {
        log.info("Process transaction with details and notify user: {}", transaction);
        return userNotificationService.notify(transaction);
    }
}
