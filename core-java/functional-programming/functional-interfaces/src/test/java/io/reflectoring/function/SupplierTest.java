package io.reflectoring.function;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SupplierTest {
  @Test
  void supplier() {
    // Supply random numbers
    Supplier<Integer> randomNumberSupplier = () -> new Random().nextInt(100);
    int result = randomNumberSupplier.get();
    Assertions.assertTrue(result >= 0 && result < 100);
  }

  @Test
  void intSupplier() {
    IntSupplier nextWinner = () -> new Random().nextInt(100, 200);
    int result = nextWinner.getAsInt();
    Assertions.assertTrue(result >= 100 && result < 200);
  }

  @Test
  void longSupplier() {
    LongSupplier nextWinner = () -> new Random().nextLong(100, 200);
    LongStream winners = LongStream.generate(nextWinner).limit(10);
    Assertions.assertEquals(10, winners.toArray().length);
  }

  @Test
  void doubleSupplier() {
    // Random data for plotting graph
    DoubleSupplier weightSupplier = () -> new Random().nextDouble(100, 200);
    DoubleStream dataSample = DoubleStream.generate(weightSupplier).limit(10);
    Assertions.assertEquals(10, dataSample.toArray().length);
  }

  @ParameterizedTest
  @CsvSource(value = {"ON,true", "OFF,false"})
  void booleanSupplier(String statusCode, boolean expected) {
    AtomicReference<String> status = new AtomicReference<>();
    status.set(statusCode);
    // Simulate a service health check
    BooleanSupplier isServiceHealthy =
        () -> {
          // Here, we could check the actual health of a service.
          // simplified for test purpose
          return status.toString().equals("ON");
        };
    boolean result = isServiceHealthy.getAsBoolean();
    Assertions.assertEquals(expected, result);
  }
}
