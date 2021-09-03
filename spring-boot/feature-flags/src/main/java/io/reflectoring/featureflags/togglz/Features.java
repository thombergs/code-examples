package io.reflectoring.featureflags.togglz;

import org.togglz.core.Feature;
import org.togglz.core.activation.GradualActivationStrategy;
import org.togglz.core.annotation.ActivationParameter;
import org.togglz.core.annotation.DefaultActivationStrategy;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.context.FeatureContext;

public enum Features implements Feature {

    GLOBAL_BOOLEAN_FLAG,

    @EnabledByDefault
    @DefaultActivationStrategy(id = GradualActivationStrategy.ID, parameters = {
            @ActivationParameter(name = GradualActivationStrategy.PARAM_PERCENTAGE, value = "50")
    })
    USER_BASED_PERCENTAGE_ROLLOUT,

    @EnabledByDefault
    @DefaultActivationStrategy(id = "clicked")
    USER_ACTION_TARGETED_FEATURE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
