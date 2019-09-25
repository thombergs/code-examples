package io.reflectoring.immutable.builder;

import org.junit.jupiter.api.Test;

class BuilderTest {

  @Test
  void builderTest(){
    User user = User.builder()
            .id(42L)
            .build();
  }

}
