package io.reflectoring.featureflags.implementations.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("features")
public class FeatureProperties {

    private boolean featureOne;
    private int featureTwo;

    public FeatureProperties() {
    }

    public boolean getFeatureOne() {
        return featureOne;
    }

    public void setFeatureOne(boolean featureOne) {
        this.featureOne = featureOne;
    }

    public int getFeatureTwo() {
        return featureTwo;
    }

    public void setFeatureTwo(int featureTwo) {
        this.featureTwo = featureTwo;
    }
}
