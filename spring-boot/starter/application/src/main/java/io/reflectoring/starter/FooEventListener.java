package io.reflectoring.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "eventstarter.enabled", havingValue = "true")
class FooEventListener extends EventListener {

	public FooEventListener(EventListenerProperties properties) {
		super(properties);
	}

	@Override
	public String getSubscribedEventType() {
		return "foo";
	}

	@Override
	public void onEvent(Event event) {
		log.info("received event {}", event);
	}
}
