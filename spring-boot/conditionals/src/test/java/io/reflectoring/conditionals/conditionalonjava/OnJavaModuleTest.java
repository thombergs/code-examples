package io.reflectoring.conditionals.conditionalonjava;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OnJavaModule.class)
class OnJavaModuleTest {

  @Autowired(required = false)
  private OnJavaModule module;

  @Test
  @Disabled("the module is loaded although the test is run on Java 11")
  void moduleIsNotLoaded(){
    assertThat(module).isNull();
  }

}