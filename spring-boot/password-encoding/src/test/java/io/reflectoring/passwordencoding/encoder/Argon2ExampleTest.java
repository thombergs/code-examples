package io.reflectoring.passwordencoding.encoder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Argon2ExampleTest {

  private Argon2Example argon2Example = new Argon2Example();

  @Test
  void encode() {
    // given
    String plainPassword = "password";

    // when
    String actual = argon2Example.encode(plainPassword);

    // then
    assertThat(actual).startsWith("$argon2id$v=19$m=4096,t=3,p=1");
  }
}
