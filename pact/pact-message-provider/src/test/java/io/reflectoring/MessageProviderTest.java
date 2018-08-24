package io.reflectoring;

import java.util.Collections;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.AmqpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(PactRunner.class)
@Provider("userservice")
@PactFolder("../pact-message-consumer/target/pacts")
@SpringBootTest
public class MessageProviderTest {

	@TestTarget
	public final Target target = new AmqpTarget(Collections.singletonList("io.reflectoring"));

	@PactVerifyProvider("a user created message")
	public String verifyUserCreatedMessage() {
		return "{\"testParam1\": \"value1\",\"testParam2\": \"value2\"}";
	}
}
