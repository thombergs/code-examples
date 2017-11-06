package com.example.demo;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRestPactRunner.class)
@Provider("userservice")
@PactBroker(host = "adesso.pact.dius.com.au/", port = "80", protocol = "https", authentication =
@PactBrokerAuth(username = "Vm6YWrQURJ1T7mDIRiKwfexCAc4HbU", password = "aLerJwBhpEcN0Wm88Wgvs45AR9dXpc")
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
		"server.port=8080"
})
public class UserControllerProviderTest {

	@MockBean
	private UserRepository userRepository;

	@TestTarget
	public final Target target = new HttpTarget(8080);

	@State("provider accepts a new person")
	public void toCreatePersonState() {
		User user = new User();
		user.setId(42L);
		user.setFirstName("Arthur");
		user.setLastName("Dent");
		when(userRepository.save(any(User.class))).thenReturn(user);
	}

}