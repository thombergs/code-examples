package io.reflectoring.eventsdemo.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.reflectoring.eventsdemo.events.UserCreatedEvent;

@Component
public class UserCreatedListener implements ApplicationListener<UserCreatedEvent> {

	@EventListener
	public void handleUserCreatedEvent(UserCreatedEvent event) {
		System.out.println("User created event triggered with annotated listener. Created user: " + event.getName());
	}
	
	@EventListener
	public void handleUserCreatedEvent2(UserCreatedEvent event) {
		System.out.println("Another listener listening to user created event: " + event.getName());
	}


	@Override
	public void onApplicationEvent(UserCreatedEvent event) {
		System.out.println("User created event triggered with ApplicationListener implemented listener. Created user: " + event.getName());
	}
}
