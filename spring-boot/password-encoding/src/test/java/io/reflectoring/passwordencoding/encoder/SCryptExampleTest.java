package io.reflectoring.passwordencoding.encoder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SCryptExampleTest {

  private SCryptExample sCryptExample = new SCryptExample();

  @Test
  void encode() {
    // given
    String plainPassword = "password";

    // when
    String actual = sCryptExample.encode(plainPassword);

    // then
    assertThat(actual).hasSize(140);
    assertThat(actual).startsWith("$e0801");
  }
}
