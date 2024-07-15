package io.reflectoring.user.deactivation.scheduler;

import org.springframework.stereotype.Component;

import lombok.NonNull;

@Component
class ScheduleNameGenerator {

    private static final String SCHEDULE_NAME_FORMAT = "user_deactivation_schedule_%s";

    public String generate(@NonNull final String userIdentifier) {
        return String.format(SCHEDULE_NAME_FORMAT, sanitize(userIdentifier));
    }

    private String sanitize(@NonNull final String userIdentifier) {
        return userIdentifier.replaceAll("[^0-9a-zA-Z-_.]", "_");
    }

}