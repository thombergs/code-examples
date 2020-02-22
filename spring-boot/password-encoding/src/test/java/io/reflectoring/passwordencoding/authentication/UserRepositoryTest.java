package io.reflectoring.passwordencoding.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  @Test
  void findUserByUsername() {
    // given
    String username = "user";

    // when
    UserCredentials userCredentials = userRepository.findByUsername(username);

    // then
    assertThat(userCredentials).isNotNull();
  }
}
