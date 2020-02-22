package io.reflectoring.passwordencoding.encoder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Pbkdf2ExampleTest {

  private Pbkdf2Example pbkdf2Example = new Pbkdf2Example();

  @Test
  void encode() {
    // given
    String plainPassword = "plainPassword";

    // when
    String actual = pbkdf2Example.encode(plainPassword);

    // then
    assertThat(actual).hasSize(80);
  }
}
