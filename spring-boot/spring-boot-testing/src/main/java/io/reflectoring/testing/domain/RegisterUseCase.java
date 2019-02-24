package io.reflectoring.testing.domain;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUseCase {

  private final SaveUserPort saveUserPort;

  private final SendMailPort sendMailPort;

  public Long registerUser(User user, boolean sendWelcomeMail) {
    user.setRegistrationDate(LocalDateTime.now());

    if(sendWelcomeMail){
      sendMailPort.sendMail("Welcome!", "Thanks for registering.");
    }

    return saveUserPort.saveUser(user);
  }

}
