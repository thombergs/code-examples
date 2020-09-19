package io.reflectoring.cache.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("embedded")
@Disabled("makes problems on Github Actions")
class CarResourceEmbeddedCacheIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void manageCar() throws Exception {
        // given
        CarDto newCarDto = CarDto.builder()
                .name("vw")
                .color("white")
                .build();

        // when
        String response = mockMvc.perform(
                post("/cars")
                        .content(objectMapper.writeValueAsString(newCarDto))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(
                post("/cars")
                        .content(objectMapper.writeValueAsString(newCarDto))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isCreated());

        CarDto carDto = objectMapper.readValue(response, CarDto.class);
        carDto.setName("bmw");

        mockMvc.perform(
                put("/cars")
                        .content(objectMapper.writeValueAsString(carDto))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isOk());

        // when
        mockMvc.perform(
                get("/cars/" + carDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/cars/" + carDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/cars/" + carDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isOk());

        // when
        mockMvc.perform(
                delete("/cars/" + carDto.getId())
        )
                // then
                .andExpect(status().isNoContent());
    }
}