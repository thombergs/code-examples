package io.reflectoring.conditionals.conditionalonclass;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "this.clazz.does.not.Exist")
class OnClassModule {

}
