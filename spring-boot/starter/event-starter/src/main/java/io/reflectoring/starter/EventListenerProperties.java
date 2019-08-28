package io.reflectoring.starter;

import java.util.Collections;
import java.util.List;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "eventstarter.listener")
@Data
class EventListenerProperties {

	/**
	 * List of event types that will be passed to {@link EventListener} implementations. All other events
	 * will be ignored.
	 */
	private List<String> enabledEvents = Collections.emptyList();

}
