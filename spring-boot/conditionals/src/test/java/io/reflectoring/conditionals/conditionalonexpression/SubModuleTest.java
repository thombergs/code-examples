package io.reflectoring.conditionals.conditionalonexpression;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(
        classes = SubModule.class,
        properties = {
                "module.enabled=true",
                "module.submodule.enabled=false"
        })
public class SubModuleTest {

  @Autowired(required = false)
  private SubModule module;

  @Test
  void moduleIsNotLoaded() {
    assertThat(module).isNull();
  }

}