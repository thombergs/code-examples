package io.reflectoring.function.custom;

/** The arithmetic operation functional interface. */
public interface ArithmeticOperation {
  /**
   * Operates on two integer inputs to calculate a result.
   *
   * @param a the first number
   * @param b the second number
   * @return the result
   */
  int operate(int a, int b);
}
