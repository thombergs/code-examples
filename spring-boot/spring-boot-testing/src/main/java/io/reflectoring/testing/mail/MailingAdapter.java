package io.reflectoring.testing.mail;

import io.reflectoring.testing.domain.SendMailPort;
import org.springframework.stereotype.Component;

@Component
public class MailingAdapter implements SendMailPort {

  @Override
  public void sendMail(String subject, String text) {
      // sending a mail...
  }

}
