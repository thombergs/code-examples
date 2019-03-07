package io.reflectoring.conditionals.applyingconditionals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ConditionalComponentTest {

  @Autowired(required = false)
  ConditionalComponent conditionalComponent;

  @Test
  void conditionalComponentIsNotLoaded(){
    assertThat(conditionalComponent).isNull();
  }

}