package io.reflectoring.service;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import io.awspring.cloud.sns.core.SnsTemplate;
import io.reflectoring.configuration.AwsSnsTopicProperties;
import io.reflectoring.dto.UserCreatedEventDto;
import io.reflectoring.dto.UserCreationRequestDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsSnsTopicProperties.class)
public class UserService {

	private final SnsTemplate snsTemplate;
	private final AwsSnsTopicProperties awsSnsTopicProperties;

	public void create(@NonNull final UserCreationRequestDto userCreationRequest) {
		// save user record in database

		final var topicArn = awsSnsTopicProperties.getTopicArn();
		final var payload = convert(userCreationRequest);
		snsTemplate.convertAndSend(topicArn, payload);
		log.info("Successfully published message to topic ARN: {}", topicArn);
	}

	private UserCreatedEventDto convert(@NonNull final UserCreationRequestDto userCreationRequest) {
		final var userCreatedEvent = new UserCreatedEventDto();
		userCreatedEvent.setName(userCreationRequest.getName());
		userCreatedEvent.setEmailId(userCreationRequest.getEmailId());
		return userCreatedEvent;
	}

}
