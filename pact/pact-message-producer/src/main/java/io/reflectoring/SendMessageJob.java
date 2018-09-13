package io.reflectoring;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;

class SendMessageJob {

	private Random random = new Random();

	private MessageProducer messageProducer;

	SendMessageJob(MessageProducer messageProducer) {
		this.messageProducer = messageProducer;
	}

	/**
	 * This scheduled job simulates the "real" business logic that should produce messages.
	 */
	@Scheduled(fixedDelay = 1000)
	void sendUserCreatedMessage() {
		try {
			UserCreatedMessage userCreatedMessage = UserCreatedMessage.builder()
							.messageUuid(UUID.randomUUID().toString())
							.user(User.builder()
											.id(random.nextLong())
											.name("Zaphpod Beeblebrox")
											.build())
							.build();
			messageProducer.produceUserCreatedMessage(userCreatedMessage);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
