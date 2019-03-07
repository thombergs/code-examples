package io.reflectoring.conditionals.conditionalonmissingclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingClass(value = "this.clazz.does.not.Exist")
class OnMissingClassModule {

}
