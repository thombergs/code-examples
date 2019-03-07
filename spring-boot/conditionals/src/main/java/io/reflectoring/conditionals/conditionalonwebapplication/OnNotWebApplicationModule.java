package io.reflectoring.conditionals.conditionalonwebapplication;

import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnNotWebApplication
class OnNotWebApplicationModule {
}
