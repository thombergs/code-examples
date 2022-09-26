package com.reflectoring;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class DeprecatedExamples {

    @Test
    public void testCurrentDate() {
        Date now = new Date();
        Date before = new Date(1661832030000L);
        assertThat(now).isAfter(before);
    }

    @Test
    public void testCustomDate() {
        System.out.println("Create date for 17 August 2022 23:30");
        int year = 2022-1900;
        int month = 8-1;
        Date customDate = new Date(year, month, 17, 23, 30);
        assertThat(customDate.getYear()).isEqualTo(year);
        assertThat(customDate.getMonth()).isEqualTo(month);
        assertThat(customDate.getDate()).isEqualTo(17);
    }

    @Test
    public void testDateWithTimezone() {
        Date before = new Date(1661832030000L);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Date after = new Date(1661832030000L);
        assertThat(before).isEqualTo(after);
    }

    @Test
    public void testMutableClasses() {
        System.out.println("Create date for 17 August 2022 23:30");
        int year = 2022-1900;
        int month = 8-1;
        Date customDate = new Date(year, month, 17, 23, 30);
        assertThat(customDate.getHours()).isEqualTo(23);
        assertThat(customDate.getMinutes()).isEqualTo(30);
        customDate.setHours(20);
        customDate.setMinutes(50);
        assertThat(customDate.getHours()).isEqualTo(20);
        assertThat(customDate.getMinutes()).isEqualTo(50);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Australia/Sydney"));
        assertThat(calendar.getTimeZone()).isEqualTo(TimeZone.getTimeZone("Australia/Sydney"));
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        assertThat(calendar.getTimeZone()).isEqualTo(TimeZone.getTimeZone("Europe/London"));
    }

    @Test
    public void testDateFormatter() {
        TimeZone zone = TimeZone.getTimeZone("Europe/London");
        DateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance(zone);
        Date date = cal.getTime();
        String strFormat = dtFormat.format(date);
        assertThat(strFormat).isNotNull();

    }

    private Date getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    @Test
    public void testSqlDates() {
        Date sqlDate = getCurrentTimestamp();
        assertThat(sqlDate).isNotEqualTo(new java.sql.Date(new Date().getTime()));
    }
}





