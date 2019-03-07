package io.reflectoring.conditionals.conditionalonwebapplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ModuleIsLoadedWithWebApplicationTest {

  @Autowired(required = false)
  private OnWebApplicationModule module;

  @Test
  void moduleIsLoaded(){
    assertThat(module).isNotNull();
  }

}