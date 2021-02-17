package io.reflectoring.resilience4j.circuitbreaker.services.delays;

public interface PotentialDelay {
  void occur();
}
