package io.reflectoring.conditionals.conditionalonsinglecandidate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OnSingleCandidateModule.class)
class OnSingleCandidateModuleTest {

  @Autowired(required = false)
  private OnSingleCandidateModule module;

  @Test
  void moduleIsNotLoaded(){
    assertThat(module).isNull();
  }

}