package io.reflectoring.conditionals.conditionalonbean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class NoOptionalBeanModule {

  @Bean
  @ConditionalOnBean(OptionalBean.class)
  DependantBean dependantBean(){
    return new DependantBean();
  }

  static class DependantBean {}

  static class OptionalBean {}

}
