package io.reflectoring.mocking;

import io.reflectoring.mocking.SendMoneyUseCase.SendMoneyCommand;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.BDDMockito.*;

class SendMoneyControllerPlainTest {

  private SendMoneyUseCase sendMoneyUseCase = Mockito.mock(SendMoneyUseCase.class);

  private SendMoneyController sendMoneyController = new SendMoneyController(sendMoneyUseCase);

  @Test
  void testSuccess(){
    // given
    SendMoneyCommand command = new SendMoneyCommand(
            1L,
            2L,
            500);
    given(sendMoneyUseCase.sendMoney(eq(command)))
            .willReturn(true);

    // when
    ResponseEntity response = sendMoneyController
            .sendMoney(1L, 2L, 500);

    // then
    then(sendMoneyUseCase)
            .should()
            .sendMoney(eq(command));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

}
