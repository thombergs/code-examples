package io.reflectoring.featureflags.ff4j;

import org.ff4j.core.FeatureStore;
import org.ff4j.core.FlippingExecutionContext;
import org.ff4j.core.FlippingStrategy;

import java.util.Map;

public class UserClickedFlippingStrategy implements FlippingStrategy {

    @Override
    public void init(String featureName, Map<String, String> initParam) {

    }

    @Override
    public Map<String, String> getInitParams() {
        return null;
    }

    @Override
    public boolean evaluate(String featureName, FeatureStore store, FlippingExecutionContext executionContext) {
        return false;
    }
}
