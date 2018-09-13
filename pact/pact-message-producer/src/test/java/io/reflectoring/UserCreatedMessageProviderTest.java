package io.reflectoring;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

@RunWith(PactRunner.class)
@Provider("userservice")
@PactFolder("../pact-message-consumer/target/pacts")
public class UserCreatedMessageProviderTest {

	@TestTarget
	public final Target target = new CustomAmqpTarget(Collections.singletonList("io.reflectoring"));

	private MessagePublisher publisher = Mockito.mock(MessagePublisher.class);

	private MessageProducer messageProducer = new MessageProducer(new ObjectMapper(), publisher);

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
		messageProducer.produceUserCreatedMessage(message);

		// then
		ArgumentCaptor<String> messageCapture = ArgumentCaptor.forClass(String.class);
		verify(publisher, times(1)).publishMessage(messageCapture.capture(), eq("user.created"));

		// returning the message
		return messageCapture.getValue();
	}
}
