package io.reflectoring.conditionals.conditionalonmissingbean;

import javax.sql.DataSource;

import io.reflectoring.conditionals.conditionalonmissingbean.OnMissingBeanModule.InMemoryDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OnMissingBeanModule.class)
class OnMissingBeanModuleTest {

  @Autowired
  private DataSource dataSource;

  @Test
  void isCustomDataSource() {
    assertThat(dataSource instanceof InMemoryDataSource).isTrue();
  }

}