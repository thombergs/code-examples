package io.reflectoring.featureflags.togglz;

import org.togglz.core.activation.Parameter;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.user.FeatureUser;

public class UserClickedActivationStrategy implements ActivationStrategy {

    @Override
    public String getId() {
        return "clicked";
    }

    @Override
    public String getName() {
        return "Rollout based on user click";
    }

    @Override
    public boolean isActive(FeatureState featureState, FeatureUser user) {
        return (Boolean) user.getAttribute("clicked");
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[0];
    }
}
