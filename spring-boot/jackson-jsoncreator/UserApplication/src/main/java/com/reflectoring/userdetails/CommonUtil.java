package com.reflectoring.userdetails;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.Month;

public class CommonUtil {

    public static LocalDate convertToDate(int year, Month month, int day) {
        return LocalDate.of(year, month, day);
    }

}
