package com.reflectoring.timezones.mapper;

import com.reflectoring.timezones.model.DateTime;
import com.reflectoring.timezones.model.DateTimeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Objects;

public class DateTimeMapper {

    private static final Logger log = LoggerFactory.getLogger(DateTimeMapper.class);

    private final Clock clock;

    public DateTimeMapper(Clock clock) {
        this.clock = clock;
    }

    public DateTime mapToDateTime(DateTimeEntity dateTimeEntity, String timezone) {
        DateTime dateTime = new DateTime();
        dateTime.setId(dateTimeEntity.getId());
        dateTime.setDateInStr(dateTimeEntity.getDateStr());
        dateTime.setDateInDefaultFormat(dateTimeEntity.getDate());
        dateTime.setLocalDate(dateTimeEntity.getLocalDate());
        dateTime.setLocalTime(dateTimeEntity.getLocalTime());
        dateTime.setLocalDateTime(dateTimeEntity.getLocalDateTime());
        dateTime.setOffsetDateTime(dateTimeEntity.getOffsetDateTime());
        log.info("Get ZoneOffset : {}", dateTimeEntity.getOffsetDateTime().getOffset());
        dateTime.setZonedDateTime(dateTimeEntity.getZonedDateTime());
        dateTime.setCreatedDateTime(dateTimeEntity.getCreatedAt());
        String zone = Objects.nonNull(timezone) ? ZoneId.of(timezone).toString() : clock.getZone().toString();
        dateTime.setApplicationTimezone(zone);
        log.info("From DB to DateTime object : {}", dateTime);
        return dateTime;
    }
}
