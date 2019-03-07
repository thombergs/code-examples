package io.reflectoring.conditionals.conditionalonjndi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OnJndiModule.class)
class OnJndiModuleTest {

  @Autowired(required = false)
  private OnJndiModule module;

  @Test
  void moduleIsNotLoaded(){
    assertThat(module).isNull();
  }

}