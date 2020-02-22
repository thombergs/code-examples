package io.reflectoring.eventsdemo;

import org.springframework.context.ApplicationEvent;

class UserCreatedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private String name;

	UserCreatedEvent(Object source, String name) {
		super(source);
		this.name = name;
	}

	String getName() {
		return this.name;
	}

}
