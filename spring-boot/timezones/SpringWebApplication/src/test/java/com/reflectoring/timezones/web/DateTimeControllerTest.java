package com.reflectoring.timezones.web;

import com.reflectoring.timezones.config.ClockConfiguration;
import com.reflectoring.timezones.model.DateTimeEntity;
import com.reflectoring.timezones.repository.DateTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ClockConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DateTimeControllerTest {

    @Autowired
    private Clock clock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DateTimeRepository repository;

    @BeforeEach
    void setup(){
        repository.deleteAll();
    }

    @Test
    public void clockTimezone() {
        System.out.println("Get zone: " + clock.getZone());
        System.out.println("Clock system default: " + Clock.systemDefaultZone());
    }

    @Test
    public void saveDateTimeObject() throws Exception {

        ResultActions response = mockMvc.perform(post("/app/v1/timezones/default"));
        response.andDo(print()).
                andExpect(status().isOk());


    }
}
