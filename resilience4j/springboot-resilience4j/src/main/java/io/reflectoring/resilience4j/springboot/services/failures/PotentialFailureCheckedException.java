package io.reflectoring.resilience4j.springboot.services.failures;

public interface PotentialFailureCheckedException {
  boolean occur() throws Exception;
}

