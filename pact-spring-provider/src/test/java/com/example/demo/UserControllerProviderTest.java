package com.example.demo;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRestPactRunner.class)
@Provider("userservice")
@PactFolder("../pact-angular/pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8080"
})
public class UserControllerProviderTest {

  @MockBean
  private UserRepository userRepository;

  @TestTarget
  public final Target target = new HttpTarget(8080);

  @State({"provider accepts a new person",
          "person 42 exists"})
  public void toCreatePersonState() {
    User user = new User();
    user.setId(42L);
    user.setFirstName("Arthur");
    user.setLastName("Dent");
    when(userRepository.findOne(eq(42L))).thenReturn(user);
    when(userRepository.save(any(User.class))).thenReturn(user);
  }

}