package io.reflectoring;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.AmqpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.*;

@RunWith(PactRunner.class)
@Provider("userservice")
@PactFolder("../pact-message-consumer/target/pacts")
@SpringBootTest
public class UserCreatedMessageProviderTest {

	@TestTarget
	public final Target target = new AmqpTarget(Collections.singletonList("io.reflectoring"));

	@Autowired
	private UserCreatedMessageProvider messageProvider;

	@MockBean
	private UserCreatedMessagePublisher publisher;

	@PactVerifyProvider("a user created message")
	public String verifyUserCreatedMessage() throws IOException {
		// given
		doNothing().when(publisher).publishMessage(any(String.class), eq("user.created"));

		// when
		UserCreatedMessage message = UserCreatedMessage.builder()
						.messageUuid(UUID.randomUUID().toString())
						.user(User.builder()
										.id(42L)
										.name("Zaphod Beeblebrox")
										.build())
						.build();
		messageProvider.sendUserCreatedMessage(message);

		// then
		ArgumentCaptor<String> messageCapture = ArgumentCaptor.forClass(String.class);
		verify(publisher, times(1)).publishMessage(messageCapture.capture(), eq("user.created"));

		// returning the message
		return messageCapture.getValue();
	}
}
