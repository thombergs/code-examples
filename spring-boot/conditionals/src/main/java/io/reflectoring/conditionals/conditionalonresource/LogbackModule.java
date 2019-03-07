package io.reflectoring.conditionals.conditionalonresource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnResource(resources = "/logback.xml")
class LogbackModule {
}
