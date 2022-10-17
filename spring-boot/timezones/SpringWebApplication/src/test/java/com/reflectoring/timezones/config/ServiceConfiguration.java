package com.reflectoring.timezones.config;

import com.reflectoring.timezones.mapper.DateTimeMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.ZoneId;

@TestConfiguration
public class ServiceConfiguration {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/London"));
        //Clock.fixed(Instant.parse("2022-11-08T09:10:20.00Z"), ZoneId.of("Europe/Berlin"));
    }

    @Bean
    public DateTimeMapper dateTimeMapper(Clock clock) {
        return new DateTimeMapper(clock);
    }
}
