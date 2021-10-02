package io.reflectoring.featureflags.patterns.ifelse;

import io.reflectoring.featureflags.FeatureFlagService;
import org.springframework.stereotype.Component;

@Component
class Service {

    private final FeatureFlagService featureFlagService;

    public Service(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    public int doSomething() {
        if (featureFlagService.isNewServiceEnabled()) {
            return 42;
        } else {
            return 1;
        }
    }
}
