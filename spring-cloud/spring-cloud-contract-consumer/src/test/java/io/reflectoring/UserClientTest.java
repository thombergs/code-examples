package io.reflectoring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(ids = "io.reflectoring:user-service:+:stubs:6565", workOffline = true)
public class UserClientTest {

  @Autowired
  private UserClient userClient;

  @Test
  public void createUserCompliesToContract() {
    User user = new User();
    user.setFirstName("Arthur");
    user.setLastName("Dent");
    IdObject id = userClient.createUser(user);
    assertThat(id.getId()).isEqualTo(42L);
  }

}
