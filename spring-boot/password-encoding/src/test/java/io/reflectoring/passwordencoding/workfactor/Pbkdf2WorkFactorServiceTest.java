package io.reflectoring.passwordencoding.workfactor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Pbkdf2WorkFactorServiceTest {

  private Pbkdf2WorkFactorService pbkdf2WorkFactorService = new Pbkdf2WorkFactorService();

  @Test
  void calculateIteration() {
    // given

    // when
    int iterationNumber = pbkdf2WorkFactorService.calculateIteration();

    // then
    assertThat(iterationNumber).isGreaterThanOrEqualTo(150000);
  }
}
