package io.reflectoring.mocking;

import io.reflectoring.mocking.SendMoneyUseCase.SendMoneyCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SendMoneyControllerMockitoAnnotationsJUnit4Test {

  @Mock
  private SendMoneyUseCase sendMoneyUseCase;

  @InjectMocks
  private SendMoneyController sendMoneyController;

  @Test
  public void testSuccess(){
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
