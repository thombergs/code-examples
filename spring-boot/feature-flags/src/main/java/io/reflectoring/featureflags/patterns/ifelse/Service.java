package io.reflectoring.featureflags.patterns.ifelse;

import io.reflectoring.featureflags.FeatureFlagService;
import org.springframework.stereotype.Component;

@Component
class Service {

    private final FeatureFlagService featureFlagService;

    public Service(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    public String doSomething() {
        if (featureFlagService.isNewServiceEnabled()) {
            return "new value";
        } else {
            return "old value";
        }
    }
}
