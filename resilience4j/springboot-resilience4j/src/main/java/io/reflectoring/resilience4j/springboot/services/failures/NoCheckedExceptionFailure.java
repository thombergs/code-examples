package io.reflectoring.resilience4j.springboot.services.failures;

public class NoCheckedExceptionFailure implements PotentialFailureCheckedException {
  @Override
  public boolean occur() throws Exception {
    return false;
  }
}