package io.reflectoring.mocking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendMoneyService implements SendMoneyUseCase {

  public SendMoneyService() {
    log.info(">>> constructing SendMoneyService! <<<");
  }

  @Override
  public boolean sendMoney(SendMoneyCommand command) {
    log.info("sending money!");
    return false;
  }

}
