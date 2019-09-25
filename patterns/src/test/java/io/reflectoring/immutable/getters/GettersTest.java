package io.reflectoring.immutable.getters;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class GettersTest {

  @Test
  void testGetters(){
    User user = new User(42L, Arrays.asList("role1", "role2"));
    user.getRoles().add("admin");
  }

}
