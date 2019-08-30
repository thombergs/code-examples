package io.reflectoring.starter;


import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Dummy implementation of an event publisher. Simply publishes an event to all listeners that
 * are subscribed to that event. This solution obviously doesn't work
 * across the boundaries of a single application context... .
 */
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {

	/**
	 * The listeners that should be notified about published events.
	 */
	private final List<EventListener> listeners;

	public void publishEvent(Event event) {
		log.info("publishing event: {}", event);
		for (EventListener listener : listeners) {
			listener.receive(event);
		}
	}


}
