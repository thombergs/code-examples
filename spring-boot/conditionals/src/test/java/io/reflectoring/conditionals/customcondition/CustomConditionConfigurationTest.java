package io.reflectoring.conditionals.customcondition;

import io.reflectoring.conditionals.customcondition.CustomConditionConfiguration.UnixBean;
import io.reflectoring.conditionals.customcondition.CustomConditionConfiguration.UnixBeanWithCustomAnnotation;
import io.reflectoring.conditionals.customcondition.CustomConditionConfiguration.WindowsAndUnixBean;
import io.reflectoring.conditionals.customcondition.CustomConditionConfiguration.WindowsOrUnixBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = CustomConditionConfiguration.class)
class CustomConditionConfigurationTest {

  @Autowired(required = false)
  UnixBean unixBean;

  @Autowired(required = false)
  UnixBeanWithCustomAnnotation unixBeanWithCustomAnnotation;

  @Autowired(required = false)
  WindowsOrUnixBean windowsOrUnixBean;

  @Autowired(required = false)
  WindowsAndUnixBean windowsAndUnixBean;

  @Test
  void linuxBeanIsLoaded() {
	if (System.getProperty("os.name").toLowerCase().contains("linux")) {
	  assertThat(unixBean).isNotNull();
	} else {
	  assertThat(unixBean).isNull();
	}
  }

  @Test
  void linuxBeanWithCustomAnnotationIsLoaded() {
	if (System.getProperty("os.name").toLowerCase().contains("linux")) {
	  assertThat(unixBeanWithCustomAnnotation).isNotNull();
	} else {
	  assertThat(unixBeanWithCustomAnnotation).isNull();
	}
  }

  @Test
  void windowsOrLinuxBeanIsLoaded() {
	if (System.getProperty("os.name").toLowerCase().contains("windows")
			|| System.getProperty("os.name").toLowerCase().contains("linux")) {
	  assertThat(windowsOrUnixBean).isNotNull();
	} else {
	  assertThat(windowsOrUnixBean).isNull();
	}
  }

  @Test
  void windowsAndLinuxBeanIsNotLoaded() {
	assertThat(windowsAndUnixBean).isNull();
  }

}