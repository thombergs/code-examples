package com.reflectoring;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DeprecatedExamples {

    @Test
    public void testCurrentDate() {
        Date now = new Date();
        System.out.println(now);
        Date before = new Date(1661832030000L);
        System.out.println(before);
    }

    @Test
    public void testCustomDate() {
        System.out.println("Create date for 17 August 2022 23:30");
        int year = 2022-1900;
        int month = 8-1;
        Date customDate = new Date(year, month, 17, 23, 30);
        System.out.println(customDate);
    }

    @Test
    public void testDateWithTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Date before = new Date(1661832030000L);
        System.out.println(before);
    }

    @Test
    public void testMutableClasses() {
        System.out.println("Create date for 17 August 2022 23:30");
        int year = 2022-1900;
        int month = 8-1;
        Date customDate = new Date(year, month, 17, 23, 30);
        System.out.println(customDate);
        customDate.setHours(20);
        customDate.setMinutes(50);
        System.out.println(customDate);

        Calendar calendar = Calendar.getInstance();
        System.out.println("Current Timezone: " + calendar.getTimeZone());
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        System.out.println("New Timezone: " + calendar.getTimeZone());
    }

    @Test
    public void testDateFormatter() {
        TimeZone zone = TimeZone.getTimeZone("Europe/London");
        DateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance(zone);
        Date date = cal.getTime();
        String strFormat = dtFormat.format(date);
        System.out.println(strFormat);

    }

    private Date getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    @Test
    public void testSqlDates() {
        Date sqlDate = getCurrentTimestamp();
    }
}





