package io.reflectoring.validation.controller.requestbody;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reflectoring.validation.Input;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ValidateRequestBodyController.class)
class ValidateRequestBodyControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void whenInputIsInvalid_thenReturnsStatus400() throws Exception {
    Input input = invalidInput();
    String body = objectMapper.writeValueAsString(input);

    mvc.perform(post("/validateBody")
            .contentType("application/json")
            .content(body))
            .andExpect(status().isBadRequest());
  }

  private Input invalidInput() {
    Input input = new Input();
    input.setIpAddress("invalid");
    input.setNumberBetweenOneAndTen(99);
    return input;
  }

  @Test
  void whenInputIsInvalid_thenReturnsStatus400WithErrorObject() throws Exception {
    Input input = invalidInput();
    String body = objectMapper.writeValueAsString(input);

    MvcResult result = mvc.perform(post("/validateBody")
            .contentType("application/json")
            .content(body))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertThat(result.getResponse().getContentAsString()).contains("violations");
  }

  @Test
  void whenInputIsValid_thenReturnsStatus200() throws Exception {
    Input input = validInput();
    String body = objectMapper.writeValueAsString(input);

    mvc.perform(post("/validateBody")
            .contentType("application/json")
            .content(body))
            .andExpect(status().isOk());
  }

  private Input validInput() {
    Input input = new Input();
    input.setIpAddress("255.255.255.255");
    input.setNumberBetweenOneAndTen(10);
    return input;
  }

}