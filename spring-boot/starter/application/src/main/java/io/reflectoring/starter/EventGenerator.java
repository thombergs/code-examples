package io.reflectoring.starter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class EventGenerator {

	private final EventPublisher eventPublisher;

	public EventGenerator(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Scheduled(fixedRate = 2000)
	void generateEvent() {
		Event event = new Event("foo", "This is a foo event");
		eventPublisher.publishEvent(event);
	}

}
