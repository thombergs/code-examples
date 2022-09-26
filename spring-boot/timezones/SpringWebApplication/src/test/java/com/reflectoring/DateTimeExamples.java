package com.reflectoring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.zone.ZoneRulesException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class DateTimeExamples {

    private Clock clock;

    @BeforeEach
    public void setClock() {
        clock = Clock.system(ZoneId.of("Australia/Sydney"));
    }

    @Test
    public void testLocalDate() {
        //Date based on the timezone set in `Clock` so that system default TZ is not considered
        LocalDate today = LocalDate.now(clock);
        assertThat(today.get(ChronoField.MONTH_OF_YEAR)).isPositive();
        assertThat(today.get(ChronoField.YEAR)).isPositive();
        assertThat(today.get(ChronoField.DAY_OF_MONTH)).isPositive();
        // No time info present. Should throw exception
        Assertions.assertThrows(UnsupportedTemporalTypeException.class, () -> {
            today.get(ChronoField.HOUR_OF_DAY);
        });

        LocalDate customDate = LocalDate.of(2022, Month.SEPTEMBER, 2);
        assertThat(customDate.getYear()).isEqualTo(2022);
        assertThat(customDate.getMonth()).isEqualTo(Month.SEPTEMBER);
        assertThat(customDate.getDayOfMonth()).isEqualTo(2);
        Assertions.assertThrows(UnsupportedTemporalTypeException.class, () -> {
            customDate.get(ChronoField.HOUR_OF_DAY);
        });

        // Offset is UTC+10 Or UTC+11
        assertThat(clock.getZone()).isEqualTo(ZoneId.of("Australia/Sydney"));
        // Offset is UTC-8 or UTC-9
        LocalDate zoneDate = LocalDate.now(ZoneId.of("America/Anchorage"));
        // Difference of 18-20 hours
        assertThat(today).isCloseTo(zoneDate, within(1, ChronoUnit.DAYS));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        assertThat(zoneDate).isEqualTo(LocalDate.parse(zoneDate.format(formatter), formatter));

        // java.time.DateTimeException: Invalid date 'SEPTEMBER 31'
        Assertions.assertThrows(DateTimeException.class, () -> {
            LocalDate.of(2022, Month.SEPTEMBER, 31);
        });
    }

    @Test
    public void testLocalTime() {
        //Time based on the timezone set in `Clock` so that system default TZ is not considered
        LocalTime now = LocalTime.now(clock);
        assertThat(now.get(ChronoField.HOUR_OF_DAY)).isPositive();
        assertThat(now.get(ChronoField.MINUTE_OF_DAY)).isPositive();
        assertThat(now.get(ChronoField.SECOND_OF_DAY)).isPositive();
        //Has no Date Info. java.time.temporal.UnsupportedTemporalTypeException: Unsupported field: MonthOfYear
        Assertions.assertThrows(UnsupportedTemporalTypeException.class, () -> {
            now.get(ChronoField.MONTH_OF_YEAR);
        });

        LocalTime customTime = LocalTime.of(21, 40, 50);
        assertThat(customTime.get(ChronoField.HOUR_OF_DAY)).isEqualTo(21);
        assertThat(customTime.get(ChronoField.MINUTE_OF_HOUR)).isEqualTo(40);
        assertThat(customTime.get(ChronoField.SECOND_OF_MINUTE)).isEqualTo(50);

        // Has offset of UTC-8 or UTC-9
        LocalTime zoneTime = LocalTime.now(ZoneId.of("America/Anchorage"));
        assertThat(now).isCloseTo(zoneTime, within(19, ChronoUnit.HOURS));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        //Should be almost same if not exact
        assertThat(LocalTime.parse(zoneTime.format(formatter))).isCloseTo(zoneTime, within(1, ChronoUnit.SECONDS));

        // Throws detailed Exception
        // java.time.DateTimeException: Invalid value for HourOfDay (valid values 0 - 23): 25
        Assertions.assertThrows(DateTimeException.class, () -> {
            LocalTime.of(25, 40, 50);
        });
    }

    @Test
    public void testLocalDateTime() {
        //Time based on the timezone set in `Clock` so that system default TZ is not considered
        LocalDateTime currentDateTime = LocalDateTime.now(clock);
        assertThat(currentDateTime.get(ChronoField.DAY_OF_MONTH)).isPositive();
        assertThat(currentDateTime.get(ChronoField.MONTH_OF_YEAR)).isPositive();
        assertThat(currentDateTime.get(ChronoField.YEAR)).isPositive();
        assertThat(currentDateTime.get(ChronoField.HOUR_OF_DAY)).isPositive();
        assertThat(currentDateTime.get(ChronoField.MINUTE_OF_DAY)).isPositive();
        assertThat(currentDateTime.get(ChronoField.SECOND_OF_DAY)).isPositive();

        // Using Clock Timezone + Local Date + LocalTime
        LocalDateTime currentUsingLocals = LocalDateTime.of(LocalDate.now(clock), LocalTime.now(clock));
        // Should be almost same if not exact
        assertThat(currentDateTime).isCloseTo(currentUsingLocals, within(5, ChronoUnit.SECONDS));

        LocalDateTime customDateTime = LocalDateTime.of(2022, Month.SEPTEMBER, 1, 10, 30, 59);
        assertThat(customDateTime.get(ChronoField.DAY_OF_MONTH)).isEqualTo(1);
        assertThat(customDateTime.get(ChronoField.MONTH_OF_YEAR)).isEqualTo(Month.SEPTEMBER.getValue());
        assertThat(customDateTime.get(ChronoField.YEAR)).isEqualTo(2022);
        assertThat(customDateTime.get(ChronoField.HOUR_OF_DAY)).isEqualTo(10);
        assertThat(customDateTime.get(ChronoField.MINUTE_OF_HOUR)).isEqualTo(30);
        assertThat(customDateTime.get(ChronoField.SECOND_OF_MINUTE)).isEqualTo(59);

        // Comparing zone offset of UTC+2 with Australia/Sydney (UTC+10 OR UTC+11)
        LocalDateTime zoneDateTime = LocalDateTime.now(ZoneId.of("+02:00"));
        assertThat(currentUsingLocals).isCloseTo(zoneDateTime, within(9, ChronoUnit.HOURS));

        String currentDateTimeStr = "20-02-2022 10:30:45";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime parsedTime = LocalDateTime.parse(currentDateTimeStr, format);
        assertThat(parsedTime.get(ChronoField.DAY_OF_MONTH)).isEqualTo(20);
        assertThat(parsedTime.get(ChronoField.MONTH_OF_YEAR)).isEqualTo(Month.FEBRUARY.getValue());
        assertThat(parsedTime.get(ChronoField.YEAR)).isEqualTo(2022);
        assertThat(parsedTime.get(ChronoField.HOUR_OF_DAY)).isEqualTo(10);
        assertThat(parsedTime.get(ChronoField.MINUTE_OF_HOUR)).isEqualTo(30);
        assertThat(parsedTime.get(ChronoField.SECOND_OF_MINUTE)).isEqualTo(45);

        //java.time.zone.ZoneRulesException: Unknown time-zone ID: Europ/London
        Assertions.assertThrows(ZoneRulesException.class, () -> {
            LocalDateTime.now(ZoneId.of("Europ/London"));
        });
    }

    @Test
    public void testZonedDateTime() {
        //Time based on the timezone set in `Clock` so that system default TZ is not considered
        ZonedDateTime currentZoneDateTime = ZonedDateTime.now(clock);
        assertThat(currentZoneDateTime.getZone()).isEqualTo(ZoneId.of("Australia/Sydney"));
        assertThat(currentZoneDateTime.get(ChronoField.DAY_OF_MONTH)).isPositive();
        assertThat(currentZoneDateTime.get(ChronoField.MONTH_OF_YEAR)).isPositive();
        assertThat(currentZoneDateTime.get(ChronoField.YEAR)).isPositive();
        assertThat(currentZoneDateTime.get(ChronoField.HOUR_OF_DAY)).isPositive();
        assertThat(currentZoneDateTime.get(ChronoField.MINUTE_OF_HOUR)).isPositive();
        assertThat(currentZoneDateTime.get(ChronoField.SECOND_OF_MINUTE)).isPositive();

        // Clock TZ + LocalDateTime + Specified ZoneId
        ZonedDateTime withLocalDateTime = ZonedDateTime.of(LocalDateTime.now(clock), ZoneId.of("Australia/Sydney"));
        // Should be almost same if not exact
        assertThat(currentZoneDateTime).isCloseTo(withLocalDateTime, within(5, ChronoUnit.SECONDS));

        // Clock TZ + LocalDate + LocalTime + Specified zone
        ZonedDateTime withLocals = ZonedDateTime.of(LocalDate.now(clock), LocalTime.now(clock), clock.getZone());
        // Should be almost same if not exact
        assertThat(withLocalDateTime).isCloseTo(withLocals, within(5, ChronoUnit.SECONDS));

        ZonedDateTime customZoneDateTime = ZonedDateTime.of(2022, Month.FEBRUARY.getValue(), MonthDay.now(clock).getDayOfMonth(), 20, 45, 50, 55, ZoneId.of("Europe/London"));
        assertThat(customZoneDateTime.getZone()).isEqualTo(ZoneId.of("Europe/London"));
        assertThat(customZoneDateTime.get(ChronoField.DAY_OF_MONTH)).isEqualTo(MonthDay.now(clock).getDayOfMonth());
        assertThat(customZoneDateTime.get(ChronoField.MONTH_OF_YEAR)).isEqualTo(Month.FEBRUARY.getValue());
        assertThat(customZoneDateTime.get(ChronoField.YEAR)).isEqualTo(2022);
        assertThat(customZoneDateTime.get(ChronoField.HOUR_OF_DAY)).isEqualTo(20);
        assertThat(customZoneDateTime.get(ChronoField.MINUTE_OF_HOUR)).isEqualTo(45);
        assertThat(customZoneDateTime.get(ChronoField.SECOND_OF_MINUTE)).isEqualTo(50);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        String timeStamp1 = "2022-03-27 10:15:30 AM"; // This String has no timezone information. Hence we need to provide one for it to be successfully parsed.
        // Has offset UTC+0 or UTC+1
        ZonedDateTime parsedZonedTime1 = ZonedDateTime.parse(timeStamp1, formatter.withZone(ZoneId.of("Europe/London")) );
        // Has offset UTC+10 or UTC+11
        ZonedDateTime parsedZonedTime2 = parsedZonedTime1.withZoneSameInstant(ZoneId.of("Australia/Sydney"));
        assertThat(parsedZonedTime1).isCloseTo(parsedZonedTime2, within(10, ChronoUnit.HOURS));
    }

    @Test
    public void testOffsetDateTime() {
        OffsetDateTime currentDateTime = OffsetDateTime.now(clock);
        // Offset can be either UTC+10 or UTC+11 depending on DST
        Assertions.assertTrue(Stream.of(ZoneOffset.of("+10:00"), ZoneOffset.of("+11:00")).anyMatch(zo ->
                zo.equals(currentDateTime.getOffset())));
        assertThat(currentDateTime.get(ChronoField.DAY_OF_MONTH)).isPositive();
        assertThat(currentDateTime.get(ChronoField.MONTH_OF_YEAR)).isPositive();
        assertThat(currentDateTime.get(ChronoField.YEAR)).isPositive();
        assertThat(currentDateTime.get(ChronoField.HOUR_OF_DAY)).isPositive();
        assertThat(currentDateTime.get(ChronoField.MINUTE_OF_HOUR)).isPositive();
        assertThat(currentDateTime.get(ChronoField.SECOND_OF_MINUTE)).isPositive();

        // For the specified offset, check the difference with the current
        // Since Offset is hardcoded, Zone rules will not apply here for +01:00 (No DST)
        ZoneOffset zoneOffSet= ZoneOffset.of("+01:00");
        OffsetDateTime offsetDateTime = OffsetDateTime.now(zoneOffSet);
        assertThat(currentDateTime).isCloseTo(offsetDateTime, within(10, ChronoUnit.HOURS));

        // Offset + LocalDate + LocalTime
        // Since Offset here is derived from Zone Id, Zone rules will apply and DST changes will be considered
        OffsetDateTime fromLocals = OffsetDateTime.of(LocalDate.now(clock), LocalTime.now(clock), currentDateTime.getOffset());
        Assertions.assertTrue(Stream.of(ZoneOffset.of("+10:00"), ZoneOffset.of("+11:00")).anyMatch(zo ->
                zo.equals(fromLocals.getOffset())));
        assertThat(currentDateTime).isCloseTo(fromLocals, within(5, ChronoUnit.SECONDS));

        OffsetDateTime fromLocalDateTime = OffsetDateTime.of(LocalDateTime.of(2022, Month.NOVEMBER, 1, 10, 10, 10), currentDateTime.getOffset());
        Assertions.assertTrue(Stream.of(ZoneOffset.of("+10:00"), ZoneOffset.of("+11:00")).anyMatch(zo ->
                zo.equals(fromLocalDateTime.getOffset())));
        assertThat(fromLocalDateTime.get(ChronoField.DAY_OF_MONTH)).isEqualTo(1);
        assertThat(fromLocalDateTime.get(ChronoField.MONTH_OF_YEAR)).isEqualTo(Month.NOVEMBER.getValue());
        assertThat(fromLocalDateTime.get(ChronoField.YEAR)).isEqualTo(2022);
        assertThat(fromLocalDateTime.get(ChronoField.HOUR_OF_DAY)).isEqualTo(10);
        assertThat(fromLocalDateTime.get(ChronoField.MINUTE_OF_HOUR)).isEqualTo(10);
        assertThat(fromLocalDateTime.get(ChronoField.SECOND_OF_MINUTE)).isEqualTo(10);

        // Defined offset based on zone rules will consider DST
        OffsetDateTime fromLocalsWithDefinedOffset = OffsetDateTime.of(LocalDate.now(clock), LocalTime.now(clock), ZoneId.of("Australia/Sydney").getRules().getOffset(LocalDateTime.of(2022, Month.NOVEMBER, 1, 10, 10, 10)));
        assertThat(fromLocalsWithDefinedOffset.getOffset()).isEqualTo(ZoneOffset.of("+11:00"));

        OffsetDateTime sameInstantDiffOffset = currentDateTime.withOffsetSameInstant(ZoneOffset.of("+01:00"));
        assertThat(currentDateTime).isCloseTo(sameInstantDiffOffset, within(10, ChronoUnit.HOURS));

        OffsetDateTime dt = OffsetDateTime.parse("2011-12-03T10:15:30+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        assertThat(fmt.format(dt)).contains("Z");

        ZonedDateTime currentZoneDateTime = ZonedDateTime.now(clock);
        OffsetDateTime convertFromZoneToOffset = currentZoneDateTime.toOffsetDateTime();
        assertThat(currentDateTime).isCloseTo(convertFromZoneToOffset, within(5, ChronoUnit.SECONDS));
    }
}
