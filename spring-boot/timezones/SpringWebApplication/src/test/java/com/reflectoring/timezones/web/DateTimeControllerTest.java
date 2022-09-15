package com.reflectoring.timezones.web;

import com.reflectoring.timezones.config.ServiceConfiguration;
import com.reflectoring.timezones.model.DateTimeEntity;
import com.reflectoring.timezones.repository.DateTimeRepository;
import com.reflectoring.timezones.service.DateTimeService;
import org.hamcrest.Matchers;
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
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ServiceConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DateTimeControllerTest {

    @Autowired
    private Clock clock;

    @Autowired
    private DateTimeService service;

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
        List<DateTimeEntity> list = repository.findAll();
        assertTrue(!list.isEmpty());
        assertTrue(list.size() == 1);
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationTimezone").value("Europe/London"))
                .andExpect(jsonPath("$.['zonedDateTime (column zoned_datetime)']", Matchers.containsString("+01:00")))
                .andExpect(jsonPath("$.['offsetDateTime (column offset_datetime)']", Matchers.containsString("+01:00")))
                .andExpect(jsonPath("$.['localDateTime (column local_datetime_dt)']", Matchers.not(Matchers.containsString("+01:00"))));
    }

    @Test
    public void saveDateTimeObjectForDst() throws Exception {
        ResultActions response = mockMvc.perform(post("/app/v1/timezones/dst"));
        List<DateTimeEntity> list = repository.findAll();
        assertTrue(!list.isEmpty());
        assertTrue(list.size() == 1);
        DateTimeEntity entity = list.get(0);
        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationTimezone").value("Europe/London"))
                .andExpect(jsonPath("$.['zonedDateTime (column zoned_datetime)']", Matchers.containsString("Z")))
                .andExpect(jsonPath("$.['offsetDateTime (column offset_datetime)']", Matchers.containsString("Z")))
                .andExpect(jsonPath("$.['localDateTime (column local_datetime_dt)']", Matchers.not(Matchers.containsString("Z"))));
    }
}
