package io.reflectoring.passwordencoding.encoder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BCryptExampleTest {

  private BCryptExample bcryptExample = new BCryptExample();

  @Test
  void encode() {
    // given
    String plainPassword = "password";

    // when
    String encoded = bcryptExample.encode(plainPassword);

    // then
    assertThat(encoded).startsWith("$2a$10");
  }
}
