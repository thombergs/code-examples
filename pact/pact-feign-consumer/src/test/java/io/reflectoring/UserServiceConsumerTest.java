package io.reflectoring;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "userservice", port = "8888")
@SpringBootTest({
				// overriding provider address
				"userservice.ribbon.listOfServers: localhost:8888"
})
class UserServiceConsumerTest {

	@Autowired
	private UserClient userClient;

	@Pact(state = "provider accepts a new person", provider = "userservice", consumer = "userclient")
	RequestResponsePact createPersonPact(PactDslWithProvider builder) {

		// @formatter:off
		return builder
						.given("provider accepts a new person")
						.uponReceiving("a request to POST a person")
							.path("/user-service/users")
							.method("POST")
						.willRespondWith()
							.status(201)
							.matchHeader("Content-Type", "application/json")
							.body(new PactDslJsonBody()
											.integerType("id", 42))
						.toPact();
		// @formatter:on
	}

	@Pact(state = "person 42 exists", provider = "userservice", consumer = "userclient")
	RequestResponsePact updatePersonPact(PactDslWithProvider builder) {
		// @formatter:off
		return builder
						.given("person 42 exists")
						.uponReceiving("a request to PUT a person")
						  .path("/user-service/users/42")
						  .method("PUT")
						.willRespondWith()
						  .status(200)
						  .matchHeader("Content-Type", "application/json")
						  .body(new PactDslJsonBody()
											.stringType("firstName", "Zaphod")
											.stringType("lastName", "Beeblebrox"))
						.toPact();
		// @formatter:on
	}


	@Test
	@PactTestFor(pactMethod = "createPersonPact")
	void verifyCreatePersonPact() {
		User user = new User();
		user.setFirstName("Zaphod");
		user.setLastName("Beeblebrox");
		IdObject id = userClient.createUser(user);
		assertThat(id.getId()).isEqualTo(42);
	}

	@Test
	@PactTestFor(pactMethod = "updatePersonPact")
	void verifyUpdatePersonPact() {
		User user = new User();
		user.setFirstName("Zaphod");
		user.setLastName("Beeblebrox");
		User updatedUser = userClient.updateUser(42L, user);
		assertThat(updatedUser.getFirstName()).isEqualTo("Zaphod");
		assertThat(updatedUser.getLastName()).isEqualTo("Beeblebrox");
	}

}
