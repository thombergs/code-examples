package io.reflectoring.conditionals.conditionalonmissingclass;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OnMissingClassModule.class)
class OnMissingClassModuleTest {

  @Autowired(required = false)
  private OnMissingClassModule module;

  @Test
  void moduleIsLoaded() {
    assertThat(module).isNotNull();
  }

}