package io.reflectoring.starter;


import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Dummy implementation of an event publisher. Simply publishes an event to all listeners that
 * are subscribed to that event. This solution obviously doesn't work
 * across JVM-boundaries... .
 */
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {

	private final List<EventListener> listeners;

	public void publishEvent(Event event) {
		log.info("publishing event: {}", event);
		for (EventListener listener : listeners) {
			listener.receive(event);
		}
	}


}
