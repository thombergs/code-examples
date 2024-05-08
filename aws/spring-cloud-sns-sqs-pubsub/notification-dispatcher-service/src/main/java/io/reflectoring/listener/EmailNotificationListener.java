package io.reflectoring.listener;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.annotation.SnsNotificationMessage;
import io.reflectoring.configuration.AwsSqsQueueProperties;
import io.reflectoring.dto.UserCreatedEventDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableConfigurationProperties(AwsSqsQueueProperties.class)
public class EmailNotificationListener {

	@SqsListener("${io.reflectoring.aws.sqs.queue-url}")
	public void listen(@SnsNotificationMessage final UserCreatedEventDto userCreatedEvent) {
		log.info("Dispatching account creation email to {} on {}", userCreatedEvent.getName(), userCreatedEvent.getEmailId());
		// business logic to send email
	}

}
