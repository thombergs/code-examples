package io.reflectoring.conditionals.applyingconditionals;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.reflectoring.conditionals.applyingconditionals.ConditionalBeanConfiguration.ConditionalBean;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = ConditionalBeanConfiguration.class)
class ConditionalBeanConfigurationTest {

  @Autowired(required = false)
  ConditionalBean conditionalBean;

  @Test
  void conditionalBeanIsNotLoaded(){
    assertThat(conditionalBean).isNull();
  }

}