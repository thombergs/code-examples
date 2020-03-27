package io.reflectoring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import(ConverterConfiguration.class)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void saveUser() {
    User user = new User(null, "Beeblebrox");
    User savedUser = userRepository.save(user);
    assertThat(userRepository.findById(savedUser.getId())).isNotNull();
  }

}