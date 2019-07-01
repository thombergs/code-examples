package io.reflectoring.testing;

import java.time.LocalDateTime;

import io.reflectoring.testing.domain.RegisterUseCase;
import io.reflectoring.testing.domain.User;
import io.reflectoring.testing.persistence.PersistenceAdapter;
import io.reflectoring.testing.persistence.UserEntity;
import io.reflectoring.testing.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class MockBeanTest {

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private RegisterUseCase registerUseCase;

  @Test
  void testRegister(){
    // given
    User user = new User("Zaphod", "zaphod@galaxy.net");
    boolean sendWelcomeMail = true;
    given(userRepository.save(any(UserEntity.class))).willReturn(userEntity(1L));

    // when
    Long userId = registerUseCase.registerUser(user, sendWelcomeMail);

    // then
    assertThat(userId).isEqualTo(1L);
  }

  private UserEntity userEntity(Long id){
    return new UserEntity(id, "Zaphod", "zaphod@galaxy.net", LocalDateTime.now());
  }
}
