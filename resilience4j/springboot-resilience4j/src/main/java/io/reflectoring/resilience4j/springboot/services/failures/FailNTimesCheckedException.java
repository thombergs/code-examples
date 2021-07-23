package io.reflectoring.resilience4j.springboot.services.failures;

public class FailNTimesCheckedException implements PotentialFailureCheckedException {
  int times;
  int failedCount;

  public FailNTimesCheckedException(int times) {
    this.times = times;
  }

  @Override
  public boolean occur() throws Exception {
    if (failedCount++ < times) {
      System.out.println("Operation failed, exception occurred");
      throw new Exception("Operation failed");
    }
    return false;
  }
}