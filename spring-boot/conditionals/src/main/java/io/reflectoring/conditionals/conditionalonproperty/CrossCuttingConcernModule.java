package io.reflectoring.conditionals.conditionalonproperty;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value="module.enabled", havingValue = "true", matchIfMissing = true)
class CrossCuttingConcernModule {
}
