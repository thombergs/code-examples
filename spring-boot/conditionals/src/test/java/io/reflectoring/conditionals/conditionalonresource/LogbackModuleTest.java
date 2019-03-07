package io.reflectoring.conditionals.conditionalonresource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = LogbackModule.class)
class LogbackModuleTest {

  @Autowired(required = false)
  private LogbackModule module;

  @Test
  void moduleIsNotLoaded(){
    assertThat(module).isNull();
  }

}