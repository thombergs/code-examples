package io.reflectoring.user.deactivation.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import lombok.NonNull;

@Component
class ScheduleExpressionGenerator {

    private static final String SCHEDULE_EXPRESSION_FORMAT = "at(%s)";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public String generate(@NonNull final LocalDateTime dateTime) {
        final var formattedDateTime = dateTime.format(DATE_TIME_FORMATTER);
        return String.format(SCHEDULE_EXPRESSION_FORMAT, formattedDateTime);
    }

}