package io.reflectoring.eventsdemo;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
class AsyncListener {

	@Async
	@EventListener
	void handleAsyncEvent(String event) {
		System.out.println(String.format("Async event recevied: %s", event));
	}

}
