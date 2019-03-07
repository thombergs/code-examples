package io.reflectoring.conditionals.customcondition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
class CustomConditionConfiguration {

  @Bean
  @Conditional(OnUnixCondition.class)
  UnixBean unixBean() {
	return new UnixBean();
  }

  @Bean
  @ConditionalOnUnix
  UnixBeanWithCustomAnnotation windowsBeanWithCustomAnnotation(){
    return new UnixBeanWithCustomAnnotation();
  }

  @Bean
  @Conditional(OnWindowsOrUnixCondition.class)
  WindowsOrUnixBean windowsOrUnixBean() {
	return new WindowsOrUnixBean();
  }

  @Bean
  @ConditionalOnUnix
  @Conditional(OnWindowsCondition.class)
  WindowsAndUnixBean windowsAndUnixBean() {
	return new WindowsAndUnixBean();
  }

  static class UnixBean {
  }

  static class UnixBeanWithCustomAnnotation {
  }

  static class WindowsOrUnixBean {
  }

  static class WindowsAndUnixBean {
  }

}
