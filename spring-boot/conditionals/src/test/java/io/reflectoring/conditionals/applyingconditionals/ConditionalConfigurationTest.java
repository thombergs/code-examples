package io.reflectoring.conditionals.applyingconditionals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = ConditionalConfiguration.class)
class ConditionalConfigurationTest {

  @Autowired(required = false)
  private ConditionalConfiguration configuration;

  @Test
  void configurationIsNotLoaded() {
	assertThat(configuration).isNull();
  }

}