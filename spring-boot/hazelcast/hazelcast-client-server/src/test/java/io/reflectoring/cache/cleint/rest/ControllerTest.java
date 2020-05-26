package io.reflectoring.cache.cleint.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reflectoring.cache.cleint.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest extends AbstractIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void putGet() throws Exception {
    // given
    String number = "BO5489";
    Car car = Car.builder().color(number).name("VW").build();

    // put
    String content = objectMapper.writeValueAsString(car);
    mockMvc
        .perform(
            post("/cars/" + number).content(content).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    // get
    String json =
        mockMvc
            .perform(get("/cars/" + number))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    Car response = objectMapper.readValue(json, Car.class);

    assertThat(response).isEqualToComparingFieldByField(car);
  }
}
