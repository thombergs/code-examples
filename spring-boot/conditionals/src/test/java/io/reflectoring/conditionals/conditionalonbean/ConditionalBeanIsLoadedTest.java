package io.reflectoring.conditionals.conditionalonbean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
import io.reflectoring.conditionals.conditionalonbean.DependantModule.DependantBean;

@SpringBootTest(classes = DependantModule.class)
class ConditionalBeanIsLoadedTest {

  @Autowired(required = false)
  private DependantBean dependantBean;

  @Test
  void dependentBeanIsLoaded() {
	assertThat(dependantBean).isNotNull();
  }

}