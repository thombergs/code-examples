package com.reflectoring;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesException;

@RunWith(JUnit4.class)
public class DateTimeExamples extends TestCase {

    @Test(expected = DateTimeException.class)
    public void testLocalDate() {
        LocalDate today = LocalDate.now();
        System.out.println("Today's Date in the dafault format : " + today);

        LocalDate customDate = LocalDate.of(2022, Month.SEPTEMBER, 2);
        System.out.println("Custom Date in the default format : " + customDate);

        LocalDate defaultZoneDate = LocalDate.now();
        System.out.println("Default Zone: " + ZoneId.systemDefault());
        LocalDate zoneDate = LocalDate.now(ZoneId.of("Europe/London"));
        System.out.println("Custom zone: " + zoneDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("Formatted Date : " + defaultZoneDate.format(formatter));

        LocalDate invalidDate = LocalDate.of(2022, Month.SEPTEMBER, 31);
        System.out.println("Invalid Date with Exception : java.time.DateTimeException: " +
                "Invalid date 'SEPTEMBER 31' : " + invalidDate);
    }

    @Test(expected = DateTimeException.class)
    public void testLocalTime() {
        LocalTime now = LocalTime.now();
        System.out.println("Current Time in default format : " + now);

        LocalTime customTime = LocalTime.of(21, 40, 50);
        System.out.println("Custom Time: " + customTime);

        LocalTime zoneTime = LocalTime.now(ZoneId.of("Europe/London"));
        System.out.println("Zone-specific time : " + zoneTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        System.out.println("Formatted Date : " + zoneTime.format(formatter));

        LocalTime invalidTime = LocalTime.of(25, 40, 50);
        System.out.println("Invalid Time: java.time.DateTimeException: " +
                "Invalid value for HourOfDay (valid values 0 - 23): 25 :=" + invalidTime);
    }

    @Test(expected = ZoneRulesException.class)
    public void testLocalDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        System.out.println("Current Date/Time in system default timezone : " + currentDateTime);

        LocalDateTime currentUsingLocals = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        System.out.println("Current Date/Time with LocalDate and LocalTime in system timezone : " + currentUsingLocals);

        LocalDateTime customDateTime = LocalDateTime.of(2022, Month.SEPTEMBER, 1, 10, 30, 59);
        System.out.println("Custom Date/Time with custom date and custom time : " + customDateTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        System.out.println("Formatted Date/Time : " + LocalDateTime.now().format(formatter));

        LocalDateTime zoneDateTime = LocalDateTime.now(ZoneId.of("+02:00"));
        System.out.println("Zoned Date Time : " + zoneDateTime);

        String currentDateTimeStr = "20-02-2022 10:30:45";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.println("Parsed From String to Object : " + LocalDateTime.parse(currentDateTimeStr, format));

        LocalDateTime invalidZoneDateTime = LocalDateTime.now(ZoneId.of("Europ/London"));
        System.out.println("Invalid Zone with Exception : java.time.zone.ZoneRulesException: " +
                "Unknown time-zone ID: Europ/London: " + invalidZoneDateTime);
    }

    @Test
    public void testZonedDateTime() {
        ZonedDateTime currentZoneDateTime = ZonedDateTime.now();
        System.out.println("Current system zone date/time : " + currentZoneDateTime);

        ZonedDateTime withLocalDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        System.out.println("Convert LocalDateTime to ZonedDateTime : " + withLocalDateTime);

        ZonedDateTime withLocals = ZonedDateTime.of(LocalDate.now(), LocalTime.now(), ZoneId.systemDefault());
        System.out.println("ZonedDateTime from LocalDate and LocalTime : " + withLocals);

        ZonedDateTime customZoneDateTime = ZonedDateTime.of(2022, Month.FEBRUARY.getValue(), MonthDay.now().getDayOfMonth(), 20, 45, 50, 55, ZoneId.of("Europe/London"));
        System.out.println("ZonedDateTime Custom : " + customZoneDateTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        String timeStamp1 = "2022-03-27 10:15:30 AM"; // This String has no timezone information. Hence we need to provide one for it to be successfully parsed.
        ZonedDateTime parsedZonedTime = ZonedDateTime.parse(timeStamp1, formatter.withZone(ZoneId.of("Europe/London")) );
        System.out.println("String to ZonedTimeStamp for Europe/London : " + parsedZonedTime);

        ZonedDateTime sameInstantDiffTimezone = parsedZonedTime.withZoneSameInstant(ZoneId.of("Asia/Calcutta"));
        System.out.println("Change from 1 timezone to another : " + sameInstantDiffTimezone);
    }

    @Test
    public void testOffsetDateTime() {
        OffsetDateTime currentDateTime = OffsetDateTime.now();
        System.out.println("System default timezone current zone offset date/time : " + currentDateTime);

        // Check for format difference
        ZonedDateTime currentZoneDateTime = ZonedDateTime.now();
        System.out.println("Current system zone date/time : " + currentZoneDateTime);

        ZoneOffset zoneOffSet= ZoneOffset.of("+01:00");
        OffsetDateTime offsetDateTime = OffsetDateTime.now(zoneOffSet);
        System.out.println("Europe/London zone offset date/time : " + offsetDateTime);

        OffsetDateTime fromLocals = OffsetDateTime.of(LocalDate.now(), LocalTime.now(), currentDateTime.getOffset());
        System.out.println("Get Offset date/time from Locals : " + fromLocals);

        OffsetDateTime fromLocalDateTime = OffsetDateTime.of(LocalDateTime.of(2022, Month.NOVEMBER, 1, 10, 10, 10), currentDateTime.getOffset());
        System.out.println("Get Offset date/time from LocalDateTime with Offset at the current Instant considered \n" +
                "(does not consider DST at custom date): " + fromLocalDateTime);

        OffsetDateTime fromLocalsWithDefinedOffset = OffsetDateTime.of(LocalDate.now(), LocalTime.now(), ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.of(2022, Month.NOVEMBER, 1, 10, 10, 10)));
        System.out.println("Get Offset date/time from Local with offset for custom LocalDateTime considered \n" +
                "(Considers DST at custom date) : " + fromLocalsWithDefinedOffset);

        OffsetDateTime sameInstantDiffOffset = currentDateTime.withOffsetSameInstant(ZoneOffset.of("+01:00"));
        System.out.println("Same instant at a different offset : " + sameInstantDiffOffset);

        OffsetDateTime dt = OffsetDateTime.parse("2011-12-03T10:15:30+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        System.out.println("OffsetDateTime parsed and formatted" + fmt.format(dt));

        OffsetDateTime convertFromZoneToOffset = currentZoneDateTime.toOffsetDateTime();
        System.out.println("Convert from ZonedDateTime to OffsetDateTime : " + convertFromZoneToOffset);

    }
}
