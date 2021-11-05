package io.reflectoring.featureflags.implementations.code;

import io.reflectoring.featureflags.implementations.FeatureFlagService;

public class CodeBackedFeatureFlagService implements FeatureFlagService {
    @Override
    public Boolean featureOne() {
        return true;
    }

    @Override
    public Integer featureTwo() {
        return 42;
    }
}
