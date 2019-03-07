package io.reflectoring.conditionals.conditionalonbean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DependantModule {

  @Bean
  OptionalBean optionalBean() {
    return new OptionalBean();
  }

  @Bean
  @ConditionalOnBean(OptionalBean.class)
  DependantBean dependantBean() {
    return new DependantBean();
  }

  static class DependantBean {
  }

  static class OptionalBean {
  }

}
