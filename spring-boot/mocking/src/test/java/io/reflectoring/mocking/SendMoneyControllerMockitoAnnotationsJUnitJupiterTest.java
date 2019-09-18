package io.reflectoring.mocking;

import io.reflectoring.mocking.SendMoneyUseCase.SendMoneyCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SendMoneyControllerMockitoAnnotationsJUnitJupiterTest {

  @Mock
  private SendMoneyUseCase sendMoneyUseCase;

  @InjectMocks
  private SendMoneyController sendMoneyController;

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
