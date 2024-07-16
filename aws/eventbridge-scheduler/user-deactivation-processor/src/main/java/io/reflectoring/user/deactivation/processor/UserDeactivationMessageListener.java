package io.reflectoring.user.deactivation.processor;

import java.util.UUID;

import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class UserDeactivationMessageListener {

    @SqsListener("${io.reflectoring.aws.sqs.queue-url}")
    public void listen(final UserDeactivationInput userDeactivationInput) {
        log.info("Deactivating account of user {}", userDeactivationInput.userId);
        // business logic to deactivate user account
    }

    record UserDeactivationInput(UUID userId) {};

}