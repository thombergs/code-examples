package io.reflectoring.conditionals.conditionalonexpression;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("${module.enabled:true} " +
		"and ${module.submodule.enabled:true}")
class SubModule {
}
