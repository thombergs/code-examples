package io.reflectoring.testing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import other.namespace.Foo;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Import(other.namespace.Foo.class)
class SpringBootImportTest {

  @Autowired
  Foo foo;

  @Test
  void test() {
    assertThat(foo).isNotNull();
  }
}
