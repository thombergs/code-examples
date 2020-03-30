package io.reflectoring.eventsdemo;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class UserRemovedListener {

	@EventListener
	ReturnedEvent handleUserRemovedEvent(UserRemovedEvent event) {
		System.out.println(String.format("User removed (@EventListerner): %s", event.getName()));
		// Spring will send ReturnedEvent as a new event
		return new ReturnedEvent();
	}

	// Listener to receive the event returned by Spring
	@EventListener
	void handleReturnedEvent(ReturnedEvent event) {
		System.out.println("ReturnedEvent received.");
	}

	@EventListener(condition = "#event.name eq 'reflectoring'")
	void handleConditionalListener(UserRemovedEvent event) {
		System.out.println(String.format("User removed (Conditional): %s", event.getName()));
	}

	@TransactionalEventListener(condition = "#event.name eq 'reflectoring'", phase = TransactionPhase.AFTER_COMPLETION)
	void handleAfterUserRemoved(UserRemovedEvent event) {
		System.out.println(String.format("User removed (@TransactionalEventListener): %s", event.getName()));
	}

}
