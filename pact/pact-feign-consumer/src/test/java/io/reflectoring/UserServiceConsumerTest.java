package io.reflectoring;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        // overriding provider address
        "userservice.ribbon.listOfServers: localhost:8888"
})
public class UserServiceConsumerTest {

  @Rule
  public PactProviderRuleMk2 stubProvider = new PactProviderRuleMk2("userservice", "localhost", 8888, this);

  @Autowired
  private UserClient userClient;

  @Pact(state = "provider accepts a new person", provider = "userservice", consumer = "userclient")
  public RequestResponsePact createPersonPact(PactDslWithProvider builder) {
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
  }

  @Pact(state = "person 42 exists", provider = "userservice", consumer = "userclient")
  public RequestResponsePact updatePersonPact(PactDslWithProvider builder) {
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
  }


  @Test
  @PactVerification(fragment = "createPersonPact")
  public void verifyCreatePersonPact() {
    User user = new User();
    user.setFirstName("Zaphod");
    user.setLastName("Beeblebrox");
    IdObject id = userClient.createUser(user);
    assertThat(id.getId()).isEqualTo(42);
  }

  @Test
  @PactVerification(fragment = "updatePersonPact")
  public void verifyUpdatePersonPact() {
    User user = new User();
    user.setFirstName("Zaphod");
    user.setLastName("Beeblebrox");
    User updatedUser = userClient.updateUser(42L, user);
    assertThat(updatedUser.getFirstName()).isEqualTo("Zaphod");
    assertThat(updatedUser.getLastName()).isEqualTo("Beeblebrox");
  }

}
