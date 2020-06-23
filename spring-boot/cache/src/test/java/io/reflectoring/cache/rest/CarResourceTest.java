package io.reflectoring.cache.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("embedded")
class CarResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveCar() throws Exception {
        // given
        CarDto carDto = CarDto.builder()
                .name("vw")
                .color("white")
                .build();

        // when
        mockMvc.perform(
                post("/cars")
                        .content(objectMapper.writeValueAsString(carDto))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isCreated());
    }

    @Test
    @Sql("/insert_car.sql")
    void updateCar() throws Exception {
        // given
        CarDto carDto = CarDto.builder()
                .id(UUID.fromString("1b104b1a-8539-4e06-aea7-9d77f2193b80"))
                .name("vw")
                .color("white")
                .build();

        // when
        mockMvc.perform(
                put("/cars")
                        .content(objectMapper.writeValueAsString(carDto))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isOk());
    }

    @Test
    @Sql("/insert_car.sql")
    void getCar() throws Exception {
        // given

        UUID id = UUID.fromString("1b104b1a-8539-4e06-aea7-9d77f2193b80");

        // when
        mockMvc.perform(
                get("/cars/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isOk());
    }

    @Test
    @Sql("/insert_car.sql")
    void deleteCar() throws Exception {
        // given

        UUID id = UUID.fromString("1b104b1a-8539-4e06-aea7-9d77f2193b80");

        // when
        mockMvc.perform(
                delete("/cars/" + id)
        )
                // then
                .andExpect(status().isNoContent());
    }
}