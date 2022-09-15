package com.reflectoring.timezones.config;

import com.reflectoring.timezones.mapper.DateTimeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class TimezoneConfiguration {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of(TimeZone.getDefault().getID()));
    }

    @Bean
    public DateTimeMapper dateTimeMapper(Clock clock) {
        return new DateTimeMapper(clock);
    }
}
