package io.reflectoring.featureflags.implementations.properties;


import io.reflectoring.featureflags.implementations.FeatureFlagService;
import org.springframework.stereotype.Component;

@Component
public class PropertiesBackedFeatureFlagService implements FeatureFlagService {

    private final FeatureProperties featureProperties;

    public PropertiesBackedFeatureFlagService(FeatureProperties featureProperties) {
        this.featureProperties = featureProperties;
    }

    @Override
    public Boolean featureOne() {
        return featureProperties.getFeatureOne();
    }

    @Override
    public Integer featureTwo() {
        return featureProperties.getFeatureTwo();
    }
}
