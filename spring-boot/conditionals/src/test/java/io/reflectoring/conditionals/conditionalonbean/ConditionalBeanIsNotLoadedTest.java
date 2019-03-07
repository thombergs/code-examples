package io.reflectoring.conditionals.conditionalonbean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = NoOptionalBeanModule.class)
class ConditionalBeanIsNotLoadedTest {

  @Autowired(required = false)
  private DependantBean dependantBean;

  @Test
  void dependentBeanIsNotLoaded() {
    assertThat(dependantBean).isNull();
  }

  static class DependantBean {}

}