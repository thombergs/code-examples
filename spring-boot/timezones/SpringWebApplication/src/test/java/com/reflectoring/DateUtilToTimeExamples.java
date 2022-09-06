package com.reflectoring;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Calendar;
import java.util.Date;


public class DateUtilToTimeExamples {

    @Test
    public void testWorkingWithLegacyDateInJava8() {
        Date date = new Date();
        System.out.println("java.util.Date : " + date);
        Instant instant = date.toInstant();
        System.out.println("Convert java.util.Date to Instant : " + instant);

        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        System.out.println("Use Instant to convert to ZonedDateTime : " + zdt);

        LocalDate ld = zdt.toLocalDate();
        System.out.println("Convert to LocalDate : " + ld);

        ZonedDateTime zdtDiffZone = zdt.withZoneSameInstant(ZoneId.of("Europe/London"));
        System.out.println("ZonedDateTime of a different zone : " + zdtDiffZone);
    }

    @Test
    public void testWorkingWithLegacyCalendarInJava8() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("java.util.Calendar : " + calendar);

        Date calendarDate = calendar.getTime();
        System.out.println("Calendar Date : " + calendarDate);

        Instant instant = calendar.toInstant();
        System.out.println("Convert java.util.Calendar to Instant : " + instant + " for timezone : " + calendar.getTimeZone());

        ZonedDateTime instantAtDiffZone = instant.atZone(ZoneId.of("Europe/London"));
        System.out.println("Instant at a different zone : " + instantAtDiffZone);

        LocalDateTime localDateTime = instantAtDiffZone.toLocalDateTime();
        System.out.println("LocalDateTime value : " + localDateTime);

    }

}
