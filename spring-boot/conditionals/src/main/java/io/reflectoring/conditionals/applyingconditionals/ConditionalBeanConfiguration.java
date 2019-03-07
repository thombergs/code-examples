package io.reflectoring.conditionals.applyingconditionals;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ConditionalBeanConfiguration {

  @Bean
  @ConditionalOnProperty("conditionalBean.enabled")
  ConditionalBean conditionalBean(){
    return new ConditionalBean();
  };


  static class ConditionalBean {

  }

}
