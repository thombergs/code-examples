package io.reflectoring.eventsdemo.events;

import org.springframework.context.ApplicationEvent;

public class UserCreatedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private String name;

	public UserCreatedEvent(String name) {
		super(name);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
