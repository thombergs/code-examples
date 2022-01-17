package io.reflectoring.featureflags.togglz;

import io.reflectoring.featureflags.FeatureFlagService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component("togglz")
public class TooglzFeatureFlagService implements FeatureFlagService {

    @Override
    public Boolean isGlobalBooleanFeatureActive() {
        return Features.GLOBAL_BOOLEAN_FLAG.isActive();
    }

    @Override
    public Boolean isPercentageRolloutActive() {
        return Features.USER_BASED_PERCENTAGE_ROLLOUT.isActive();
    }

    @Override
    public Integer getNumberFlag() {
        return null;
    }

    @Override
    public Boolean isUserActionTargetedFeatureActive() {
        return Features.USER_ACTION_TARGETED_FEATURE.isActive();
    }

    @Override
    public Boolean isNewServiceEnabled() {
        return false;
    }

    @Override
    public Optional<LocalDateTime> currentDateForWelcomeMessage() {
        // not supported
        return Optional.of(LocalDateTime.now());
    }

    @Override
    public Optional<LocalDateTime> currentDateForWelcomeEmails() {
        // not supported
        return Optional.of(LocalDateTime.now());
    }

    @Override
    public Optional<LocalDateTime> currentDateForWelcomeMessage(String username) {
        // not supported
        return Optional.of(LocalDateTime.now());
    }

    @Override
    public Optional<LocalDateTime> currentDateForWelcomeEmails(String username) {
        // not supported
        return Optional.of(LocalDateTime.now());
    }


}
