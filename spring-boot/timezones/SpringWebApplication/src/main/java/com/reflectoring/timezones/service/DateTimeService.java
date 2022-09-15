package com.reflectoring.timezones.service;

import com.reflectoring.timezones.model.DateTimeEntity;
import com.reflectoring.timezones.repository.DateTimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class DateTimeService {

    private static final Logger log = LoggerFactory.getLogger(DateTimeService.class);

    private final DateTimeRepository repository;

    private final Clock clock;

    public DateTimeService(DateTimeRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public List<DateTimeEntity> saveDateTime() {
        final ZoneId zoneId = clock.getZone();
        log.info("Timezone is : {}", clock.getZone());

        LocalDateTime dateTime = LocalDateTime.of(2022, 9, 8, 21, 21, 17);
        OffsetDateTime current = OffsetDateTime.of(dateTime, zoneId.getRules().getOffset(dateTime));

        DateTimeEntity dateTimeObj = new DateTimeEntity();
        log.info("To Date In String : {}", current.toString());
        dateTimeObj.setDateStr(current.toString());

        log.info("To java.util.Date : {}", Date.from(current.toInstant()));
        dateTimeObj.setDate(Date.from(current.toInstant()));

        log.info(" To LocalDate : {}", current.toLocalDate());
        dateTimeObj.setLocalDate(current.toLocalDate());

        log.info("To LocalTime : {}", current.toLocalTime());
        dateTimeObj.setLocalTime(current.toLocalTime());

        log.info("To LocalDateTime : {}", current.toLocalDateTime());
        dateTimeObj.setLocalDateTime(current.toLocalDateTime());

        log.info("To OffsetDateTime : {}", current);
        dateTimeObj.setOffsetDateTime(current);

        log.info("To ZonedDateTime : {}", current.atZoneSameInstant(zoneId));
        dateTimeObj.setZonedDateTime(current.atZoneSameInstant(zoneId));

        repository.save(dateTimeObj);
        return repository.findAll();
    }

    public List<DateTimeEntity> saveCustomDateTime() {
        final ZoneId zoneId = clock.getZone();
        log.info("Timezone is : {}", zoneId);

        LocalDateTime dateTime = LocalDateTime.of(2022, 11, 8, 9, 10, 20);
        OffsetDateTime custom = OffsetDateTime.of(dateTime, zoneId.getRules().getOffset(dateTime));
        log.info("Save OffsetDateTime 8th Nov 2022 : {}", custom);

        DateTimeEntity dateTimeObj = new DateTimeEntity();
        log.info("To Date In String 8th Nov 2022 : {}", custom.toString());
        dateTimeObj.setDateStr(custom.toString());

        log.info("To java.util.Date 8th Nov 2022 : {}", Date.from(custom.toInstant()));
        dateTimeObj.setDate(Date.from(custom.toInstant()));

        log.info(" To LocalDate 8th Nov 2022 : {}", custom.toLocalDate());
        dateTimeObj.setLocalDate(custom.toLocalDate());

        log.info("To LocalTime 8th Nov 2022 : {}", custom.toLocalTime());
        dateTimeObj.setLocalTime(custom.toLocalTime());

        log.info("To LocalDateTime 8th Nov 2022 : {}", custom.toLocalDateTime());
        dateTimeObj.setLocalDateTime(custom.toLocalDateTime());

        log.info("To OffsetDateTime 8th Nov 2022 : {}", custom);
        dateTimeObj.setOffsetDateTime(custom.withOffsetSameInstant(ZoneOffset.UTC));

        log.info("To ZonedDateTime 8th Nov 2022 : {}", custom.atZoneSameInstant(zoneId));
        dateTimeObj.setZonedDateTime(
                custom.atZoneSameInstant(zoneId));

        repository.save(dateTimeObj);
        return repository.findAll();
    }

    public DateTimeEntity fetchDateTime(Integer id) {
        return repository.getOne(id);
    }
}
