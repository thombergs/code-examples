package io.reflectoring.featureflags.togglz;

import io.reflectoring.featureflags.FeatureFlagService;
import org.springframework.stereotype.Component;

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

}
