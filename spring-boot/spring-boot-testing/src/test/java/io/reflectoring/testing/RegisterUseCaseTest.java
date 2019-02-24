package io.reflectoring.testing;

import io.reflectoring.testing.domain.RegisterUseCase;
import io.reflectoring.testing.domain.SaveUserPort;
import io.reflectoring.testing.domain.SendMailPort;
import io.reflectoring.testing.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.AdditionalAnswers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUseCaseTest {

  @Mock
  private SaveUserPort saveUserPort;

  @Mock
  private SendMailPort sendMailPort;

  private RegisterUseCase registerUseCase;

  @BeforeEach
  void initUseCase() {
    registerUseCase = new RegisterUseCase(saveUserPort, sendMailPort);
  }

  @Test
  void savedUserHasRegistrationDate() {
    User user = new User("zaphod", "zaphod@mail.com");
    when(saveUserPort.saveUser(any(User.class))).thenReturn(42L);
    Long userId = registerUseCase.registerUser(user, false);
    assertThat(userId).isNotNull();
  }

}