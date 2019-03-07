package io.reflectoring.conditionals.conditionalonclass;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OnClassModule.class)
class OnClassModuleTest {

  @Autowired(required = false)
  private OnClassModule module;

  @Test
  void moduleIsNotLoaded() {
    assertThat(module).isNull();
  }

}