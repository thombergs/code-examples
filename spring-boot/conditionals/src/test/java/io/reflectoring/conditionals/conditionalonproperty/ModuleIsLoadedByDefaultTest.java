package io.reflectoring.conditionals.conditionalonproperty;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ModuleIsLoadedByDefaultTest {

  @Autowired(required = false)
  private CrossCuttingConcernModule module;

  @Test
  void moduleIsLoaded() {
    assertThat(module).isNotNull();
  }

}