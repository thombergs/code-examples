package com.reflectoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


public class DateUtilToTimeExamples {

    private Clock clock;

    @BeforeEach
    public void setClock() {
        clock = Clock.system(ZoneId.of("Australia/Sydney"));
    }

    @Test
    public void testWorkingWithLegacyDateInJava8() {
        Date date = new Date();
        Instant instant = date.toInstant();
        assertThat(instant).isNotEqualTo(clock.instant());

        ZonedDateTime zdt = instant.atZone(clock.getZone());
        assertThat(zdt.getZone()).isEqualTo(ZoneId.of("Australia/Sydney"));

        LocalDate ld = zdt.toLocalDate();
        assertThat(ld).isEqualTo(LocalDate.now(clock));
        ZonedDateTime zdtDiffZone = zdt.withZoneSameInstant(ZoneId.of("Europe/London"));
        assertThat(zdtDiffZone.getZone()).isEqualTo(ZoneId.of("Europe/London"));
    }

    @Test
    public void testWorkingWithLegacyCalendarInJava8() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(clock.getZone()));
        assertThat(calendar.getTimeZone()).isEqualTo(TimeZone.getTimeZone("Australia/Sydney"));

        Date calendarDate = calendar.getTime();
        Instant instant = calendar.toInstant();
        assertThat(calendarDate.toInstant()).isEqualTo(calendar.toInstant());

        ZonedDateTime instantAtDiffZone = instant.atZone(ZoneId.of("Europe/London"));
        assertThat(instantAtDiffZone.getZone()).isEqualTo(ZoneId.of("Europe/London"));

        LocalDateTime localDateTime = instantAtDiffZone.toLocalDateTime();
        LocalDateTime localDateTimeWithZone = LocalDateTime.now(ZoneId.of("Europe/London"));
        assertThat(localDateTime).isCloseTo(localDateTimeWithZone, within(5, ChronoUnit.SECONDS));

    }

}
