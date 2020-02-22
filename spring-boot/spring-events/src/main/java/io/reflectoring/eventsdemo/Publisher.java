package io.reflectoring.eventsdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class Publisher {

	@Autowired
	private ApplicationEventPublisher publisher;

	void publishEvent() {
		// Async Event		
		publisher.publishEvent("I'm Async");
		
		
		// @EventListener Annotated and ApplicationListener			
	    publisher.publishEvent(new UserCreatedEvent(this, "Lucario"));
	    publisher.publishEvent(new UserRemovedEvent("Lucario"));
	    
	    // Conditional Listener	    
	    publisher.publishEvent(new UserCreatedEvent(this, "reflectoring"));
	    publisher.publishEvent(new UserRemovedEvent("reflectoring"));
	    
	}
}
