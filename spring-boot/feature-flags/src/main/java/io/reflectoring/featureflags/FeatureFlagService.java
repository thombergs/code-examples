package io.reflectoring.featureflags;

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

}
