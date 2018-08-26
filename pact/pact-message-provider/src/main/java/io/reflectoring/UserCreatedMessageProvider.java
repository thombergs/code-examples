package io.reflectoring;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Takes a {@link UserCreatedMessage}, converts it to a {@link String} and sends it to be published.
 */
class UserCreatedMessageProvider {

	private Logger logger = LoggerFactory.getLogger(UserCreatedMessageProvider.class);

	private ObjectMapper objectMapper;

	private UserCreatedMessagePublisher userCreatedMessagePublisher;

	UserCreatedMessageProvider(ObjectMapper objectMapper, UserCreatedMessagePublisher userCreatedMessagePublisher) {
		this.objectMapper = objectMapper;
		this.userCreatedMessagePublisher = userCreatedMessagePublisher;
	}

	void sendUserCreatedMessage(UserCreatedMessage message) throws IOException {
		String stringMessage = objectMapper.writeValueAsString(message);
		userCreatedMessagePublisher.publishMessage(stringMessage, "user.created");
		logger.info("Published message '{}'", stringMessage);
	}

}
