package com.reflectoring.timezones.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.ZoneId;

@TestConfiguration
public class ClockConfiguration {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/London"));
        //Clock.fixed(Instant.parse("2022-11-08T09:10:20.00Z"), ZoneId.of("Europe/Berlin"));
    }
}
