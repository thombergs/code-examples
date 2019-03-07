package io.reflectoring.conditionals.applyingconditionals;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("configuration.enabled")
class ConditionalConfiguration {
}
