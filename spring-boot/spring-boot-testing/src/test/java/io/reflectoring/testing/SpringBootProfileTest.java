package io.reflectoring.testing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SpringBootProfileTest {

  @Value("${foo}")
  String foo;

  @Test
  void test(){
    assertThat(foo).isEqualTo("bar");
  }
}
