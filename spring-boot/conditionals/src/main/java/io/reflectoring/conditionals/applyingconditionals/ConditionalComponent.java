package io.reflectoring.conditionals.applyingconditionals;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("conditionalComponent.enabled")
class ConditionalComponent {

}
