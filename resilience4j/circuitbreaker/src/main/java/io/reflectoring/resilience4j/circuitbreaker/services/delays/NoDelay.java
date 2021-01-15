package io.reflectoring.resilience4j.circuitbreaker.services.delays;

public class NoDelay implements PotentialDelay {
  @Override
  public void occur() {
  }
}
