package io.reflectoring.testing.domain;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUseCase {

  private final SaveUserPort saveUserPort;

  public Long registerUser(User user, boolean sendWelcomeMail) {
    user.setRegistrationDate(LocalDateTime.now());
    return saveUserPort.saveUser(user);
  }

}
