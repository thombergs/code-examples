package io.reflectoring.validation.controller.parameters;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import lombok.SneakyThrows;

@WebMvcTest(controllers = ValidateParametersController.class)
class ValidateParametersControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  @SneakyThrows
  void whenPathVariableIsInvalid_thenReturnsStatus400() {
    mvc.perform(get("/validate-path-variable/3"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value(startsWith("Validation error:")));
  }

  @Test
  @SneakyThrows
  void whenRequestParameterIsInvalid_thenReturnsStatus400() {
    mvc.perform(get("/validate-request-parameter")
            .param("param", "3"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value(startsWith("Validation error:")));
  }

  @Test
  @SneakyThrows
  void whenPathVariableIsValid_thenReturnsStatus200() {
    mvc.perform(get("/validate-path-variable/10"))
            .andExpect(status().isOk());
  }
  
  @Test
  @SneakyThrows
  void whenRequestParameterIsValid_thenReturnsStatus200() {
    mvc.perform(get("/validate-request-parameter")
    		.param("param", "10"))
            .andExpect(status().isOk());
  }

}