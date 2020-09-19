package io.reflectoring.resilience4j.bulkhead.utils;

import io.github.resilience4j.bulkhead.ContextPropagator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RequestTrackingIdPropagator implements ContextPropagator {
  @Override
  public Supplier<Optional> retrieve() {
    System.out.println("Getting request tracking id from thread: " + Thread.currentThread().getName());
    return () -> Optional.of(RequestTrackingIdHolder.getRequestTrackingId());
  }

  @Override
  public Consumer<Optional> copy() {
    return optional -> {
      System.out.println("Setting request tracking id " + optional.get() + " on thread: " + Thread.currentThread().getName());
      optional.ifPresent(s -> RequestTrackingIdHolder.setRequestTrackingId(s.toString()));
    };
  }

  @Override
  public Consumer<Optional> clear() {
    return optional -> {
      System.out.println("Clearing request tracking id on thread: " + Thread.currentThread().getName());
      optional.ifPresent(s -> RequestTrackingIdHolder.clear());
    };
  }
}