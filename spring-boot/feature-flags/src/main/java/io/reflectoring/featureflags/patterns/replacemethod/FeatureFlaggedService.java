package io.reflectoring.featureflags.patterns.replacemethod;

import io.reflectoring.featureflags.FeatureFlagService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("replaceMethodFeatureFlaggedService")
@Primary
class FeatureFlaggedService implements Service {

    private final FeatureFlagService featureFlagService;
    private final NewService newService;
    private final OldService oldService;

    public FeatureFlaggedService(FeatureFlagService featureFlagService, NewService newService, OldService oldService) {
        this.featureFlagService = featureFlagService;
        this.newService = newService;
        this.oldService = oldService;
    }

    @Override
    public int doSomething() {
        if (featureFlagService.isNewServiceEnabled()) {
            return newService.doSomething();
        } else {
            return oldService.doSomething();
        }
    }

}
