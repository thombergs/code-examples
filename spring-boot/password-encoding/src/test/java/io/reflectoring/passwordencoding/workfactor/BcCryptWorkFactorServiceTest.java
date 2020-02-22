package io.reflectoring.passwordencoding.workfactor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BcCryptWorkFactorServiceTest {

  private BcCryptWorkFactorService bcCryptWorkFactorService = new BcCryptWorkFactorService();

  @Test
  void calculateStrength() {
    // given

    // when
    int strength = bcCryptWorkFactorService.calculateStrength();

    // then
    assertThat(strength).isBetween(4, 31);
  }

  @Test
  void calculateStrengthBi() {
    // given

    // when
    BcryptWorkFactor bcryptWorkFactor =
        bcCryptWorkFactorService.calculateStrengthDivideAndConquer();

    // then
    assertThat(bcryptWorkFactor.getStrength()).isBetween(4, 31);
  }

  @Test
  void findCloserToShouldReturnNumber1IfItCloserToGoalThanNumber2() {
    // given
    int number1 = 950;
    int number2 = 1051;

    // when
    boolean actual = bcCryptWorkFactorService.isPreviousDurationCloserToGoal(number1, number2);

    // then
    assertThat(actual).isTrue();
  }

  @Test
  void findCloserToShouldReturnNUmber2IfItCloserToGoalThanNumber1() {
    // given
    int number1 = 1002;
    int number2 = 999;

    // when
    boolean actual = bcCryptWorkFactorService.isPreviousDurationCloserToGoal(number1, number2);

    // then
    assertThat(actual).isFalse();
  }

  @Test
  void findCloserToShouldReturnGoalIfNumber2IsEqualGoal() {
    // given
    int number1 = 999;
    int number2 = 1000;

    // when
    boolean actual = bcCryptWorkFactorService.isPreviousDurationCloserToGoal(number1, number2);

    // then
    assertThat(actual).isFalse();
  }

  @Test
  void findCloserToShouldReturnGoalIfNumber1IsEqualGoal() {
    // given
    int number1 = 1000;
    int number2 = 1001;

    // when
    boolean actual = bcCryptWorkFactorService.isPreviousDurationCloserToGoal(number1, number2);

    // then
    assertThat(actual).isTrue();
  }

  @Test
  void getStrengthShouldReturn4IfStrengthIs4() {
    // given
    int currentStrength = 4;

    // when
    int actual = bcCryptWorkFactorService.getStrength(0, 0, currentStrength);

    // then
    assertThat(actual).isEqualTo(4);
  }

  @Test
  void getStrengthShouldReturnPreviousStrengthIfPreviousDurationCloserToGoal() {
    // given

    // when
    int actual = bcCryptWorkFactorService.getStrength(980, 1021, 5);

    // then
    assertThat(actual).isEqualTo(4);
  }

  @Test
  void getStrengthShouldReturnCurrentStrengthIfCurrentDurationCloserToGoal() {
    // given

    // when
    int actual = bcCryptWorkFactorService.getStrength(960, 1021, 5);

    // then
    assertThat(actual).isEqualTo(5);
  }
}
