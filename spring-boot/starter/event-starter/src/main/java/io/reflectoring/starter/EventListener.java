package io.reflectoring.starter;

import lombok.RequiredArgsConstructor;

/**
 * Interface to be implemented for all event types that the application should react to.
 * EventListeners will be automatically included when they are part of the application context.
 */
@RequiredArgsConstructor
public abstract class EventListener {

	private final EventListenerProperties properties;

	public void receive(Event event) {
		if(isEnabled(event) && isSubscribed(event)){
			onEvent(event);
		}
	}

	private boolean isSubscribed(Event event) {
		return event.getType().equals(getSubscribedEventType());
	}

	private boolean isEnabled(Event event) {
		return properties.getEnabledEvents().contains(event.getType());
	}

	protected abstract String getSubscribedEventType();

	protected abstract void onEvent(Event event);

}
