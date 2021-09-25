package io.reflectoring.featureflags.patterns.replacebean;

import io.reflectoring.featureflags.FeatureFlagService;
import org.springframework.stereotype.Component;

@Component("replaceBeanFeatureFlaggedService")
class FeatureFlaggedService extends FeatureFlaggedBean<Service> {

    public FeatureFlaggedService(FeatureFlagService featureFlagService) {
        super(Service.class, featureFlagService::isNewServiceEnabled, new NewService(), new OldService());
    }
}
