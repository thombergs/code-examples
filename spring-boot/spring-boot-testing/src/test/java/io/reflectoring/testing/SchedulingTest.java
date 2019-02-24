package io.reflectoring.testing;

import io.reflectoring.SchedulingConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "io.reflectoring.scheduling.enabled=false")
class SchedulingTest {

  @Autowired(required = false)
  private SchedulingConfiguration schedulingConfiguration;

  @Test
  void test() {
    assertThat(schedulingConfiguration).isNull();
  }
}
