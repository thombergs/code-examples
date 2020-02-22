package io.reflectoring.eventsdemo;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncListener {

	@Async
	@EventListener
	public void handleAsyncEvent(String event) {
		System.out.println(String.format("Async event recevied: %s", event));
	}

}
