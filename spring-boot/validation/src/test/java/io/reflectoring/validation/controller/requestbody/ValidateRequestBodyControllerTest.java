package io.reflectoring.validation.controller.requestbody;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.reflectoring.validation.Input;
import lombok.SneakyThrows;

@WebMvcTest(controllers = ValidateRequestBodyController.class)
class ValidateRequestBodyControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  void whenInputIsInvalid_thenReturnsStatus400() {
    Input input = invalidInput();
    String body = objectMapper.writeValueAsString(input);

    mvc.perform(post("/validate-request-body")
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
  @SneakyThrows
  void whenInputIsInvalid_thenReturnsStatus400WithErrorObject() {
    Input input = invalidInput();
    String body = objectMapper.writeValueAsString(input);

    MvcResult result = mvc.perform(post("/validate-request-body")
            .contentType("application/json")
            .content(body))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertThat(result.getResponse().getContentAsString()).contains("violations");
  }

  @Test
  @SneakyThrows
  void whenInputIsValid_thenReturnsStatus200() {
    Input input = validInput();
    String body = objectMapper.writeValueAsString(input);

    mvc.perform(post("/validate-request-body")
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