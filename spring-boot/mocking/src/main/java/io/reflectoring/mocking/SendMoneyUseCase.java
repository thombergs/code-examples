package io.reflectoring.mocking;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

public interface SendMoneyUseCase {

  boolean sendMoney(SendMoneyCommand command);

  @Value
  @Getter
  @EqualsAndHashCode(callSuper = false)
  class SendMoneyCommand {

    private final Long sourceAccountId;
    private final Long targetAccountId;
    private final Integer money;

    public SendMoneyCommand(
            Long sourceAccountId,
            Long targetAccountId,
            Integer money) {
      this.sourceAccountId = sourceAccountId;
      this.targetAccountId = targetAccountId;
      this.money = money;
    }
  }

}
