package io.reflectoring.conditionals.customcondition;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.context.annotation.Conditional;

class OnWindowsAndLinuxCondition extends AllNestedConditions {

  OnWindowsAndLinuxCondition() {
	super(ConfigurationPhase.REGISTER_BEAN);
  }

  @Conditional(OnWindowsCondition.class)
  static class OnWindows {
  }

  @Conditional(OnUnixCondition.class)
  static class OnLinux {
  }

}
