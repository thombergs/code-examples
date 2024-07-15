package io.reflectoring.user.deactivation.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(UserAccountProperties.class)
class UserAccountService {

    private final ScheduleRegistrar scheduleRegistrar;
    private final UserAccountProperties userAccountProperties;
    private final ScheduleNameGenerator scheduleNameGenerator;
    private final ScheduleExpressionGenerator scheduleExpressionGenerator;

    public void deactivateAccount(@NonNull final UUID userId) {
        final var scheduleName = scheduleNameGenerator.generate(String.valueOf(userId));

        final var deactivationDateTime = LocalDateTime.now(ZoneOffset.UTC).plus(userAccountProperties.getDeactivationDelay());
        final var scheduleExpression = scheduleExpressionGenerator.generate(deactivationDateTime);

        final var input = new HashMap<String, String>();
        input.put("userId", String.valueOf(userId));

        scheduleRegistrar.register(scheduleName, scheduleExpression, input);
    }

    public void cancelAccountDeactivation(@NonNull final UUID userId) {
        final var scheduleName = scheduleNameGenerator.generate(String.valueOf(userId));
        scheduleRegistrar.delete(scheduleName);
    }

}