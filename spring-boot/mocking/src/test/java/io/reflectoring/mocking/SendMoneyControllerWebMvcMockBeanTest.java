package io.reflectoring.mocking;

import io.reflectoring.mocking.SendMoneyUseCase.SendMoneyCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SendMoneyController.class)
class SendMoneyControllerWebMvcMockBeanTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SendMoneyUseCase sendMoneyUseCase;

  @Test
  void testSendMoney() throws Exception {

    // given
    SendMoneyCommand command = new SendMoneyCommand(
            1L,
            2L,
            500);
    given(sendMoneyUseCase.sendMoney(eq(command)))
            .willReturn(true);

    // when
    mockMvc.perform(post("/sendMoney/{sourceAccountId}/{targetAccountId}/{amount}",
            1L, 2L, 500)
            .header("Content-Type", "application/json"))
            .andExpect(status().isOk());

    // then
    then(sendMoneyUseCase).should()
            .sendMoney(eq(new SendMoneyCommand(
                    1L,
                    2L,
                    500)));
  }

}
