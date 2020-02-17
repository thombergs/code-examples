package com.lucario.eventsdemo;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.reflectoring.eventsdemo.events.UserCreatedEvent;
import io.reflectoring.eventsdemo.listeners.UserCreatedListener;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class EventsDemoApplicationTests {

	@Mock
	private ApplicationEventPublisher applicationEventPublisher;

	@SpyBean
	UserCreatedListener userCreatedListener;

	@Captor
	protected ArgumentCaptor<UserCreatedEvent> publishEventCaptor;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void contextLoads() {
		System.out.println("Gere");
		 verifyPublishedEvents(new UserCreatedEvent("Lucario"));
	}
	
	protected void verifyPublishedEvents(Object... events) {
	    Mockito.verify(applicationEventPublisher, Mockito.times(events.length)).publishEvent(publishEventCaptor.capture());
	    List<UserCreatedEvent> capturedEvents = publishEventCaptor.getAllValues();

	    for (int i = 0; i < capturedEvents.size(); i++) {
	        Assert.assertEquals(capturedEvents.get(i), instanceOf(events[i].getClass()));
	        System.out.println(events[i].getClass().getName());
	        Assert.assertEquals(capturedEvents.get(i), events[i]);
	    }
	}

}
