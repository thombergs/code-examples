package io.reflectoring.featureflags;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FeatureFlagService {

    /**
     * A simple Boolean feature flag that returns either true or false for all users.
     */
    Boolean isGlobalBooleanFeatureActive();

    /**
     * A Boolean feature flag that is targeted at a cohort of users.
     */
    Boolean isPercentageRolloutActive();

    /**
     * A number-based feature flag that returns a number controlled by the Feature Flag service.
     */
    Integer getNumberFlag();

    /**
     * A feature flag based on a previous user action.
     */
    Boolean isUserActionTargetedFeatureActive();

    Boolean isNewServiceEnabled();

    /**
     * Returns the current time to be used by the welcome message feature.
     */
    Optional<LocalDateTime> currentDateForWelcomeMessage();

    /**
     * Returns the current time to be used by the welcome email feature.
     */
    Optional<LocalDateTime> currentDateForWelcomeEmails();

    Optional<LocalDateTime> currentDateForWelcomeMessage(String username);

    /**
     * Returns the current time to be used by the welcome email feature.
     */
    Optional<LocalDateTime> currentDateForWelcomeEmails(String username);

}
