package io.reflectoring.function;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.DoubleStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ConsumerTest {
  @Test
  void consumer() {
    Consumer<List<String>> trim =
        strings -> {
          if (strings != null) {
            strings.replaceAll(s -> s == null ? null : s.trim());
          }
        };
    Consumer<List<String>> upperCase =
        strings -> {
          if (strings != null) {
            strings.replaceAll(s -> s == null ? null : s.toUpperCase());
          }
        };

    List<String> input = null;
    input = Arrays.asList(null, "", " Joy", " Joy ", "Joy ", "Joy");
    trim.accept(input);
    Assertions.assertEquals(Arrays.asList(null, "", "Joy", "Joy", "Joy", "Joy"), input);

    input = Arrays.asList(null, "", " Joy", " Joy ", "Joy ", "Joy");
    trim.andThen(upperCase).accept(input);
    Assertions.assertEquals(Arrays.asList(null, "", "JOY", "JOY", "JOY", "JOY"), input);
  }

  @Test
  void biConsumer() {
    BiConsumer<List<Double>, Double> discountRule =
        (prices, discount) -> {
          if (prices != null && discount != null) {
            prices.replaceAll(price -> price * discount);
          }
        };
    BiConsumer<List<Double>, Double> bulkDiscountRule =
        (prices, discount) -> {
          if (prices != null && discount != null && prices.size() > 2) {
            // 20% discount cart has 2 items or more
            prices.replaceAll(price -> price * 0.80);
          }
        };

    double discount = 0.90; // 10% discount
    List<Double> prices = null;
    prices = Arrays.asList(20.0, 30.0, 100.0);
    discountRule.accept(prices, discount);
    Assertions.assertEquals(Arrays.asList(18.0, 27.0, 90.0), prices);

    prices = Arrays.asList(20.0, 30.0, 100.0);
    discountRule.andThen(bulkDiscountRule).accept(prices, discount);
    Assertions.assertEquals(Arrays.asList(14.4, 21.6, 72.0), prices);
  }

  @ParameterizedTest
  @CsvSource({
    "15,Turning off AC.",
    "22,---",
    "25,Turning on AC.",
    "52,Alert! Temperature not safe for humans."
  })
  void intConsumer(int temperature, String expected) {
    AtomicReference<String> message = new AtomicReference<>();
    IntConsumer temperatureSensor =
        t -> {
          message.set("---");
          if (t <= 20) {
            message.set("Turning off AC.");
          } else if (t >= 24 && t <= 50) {
            message.set("Turning on AC.");
          } else if (t > 50) {
            message.set("Alert! Temperature not safe for humans.");
          }
        };

    temperatureSensor.accept(temperature);
    Assertions.assertEquals(expected, message.toString());
  }

  @Test
  void longConsumer() {
    long duration = TimeUnit.MINUTES.toMillis(20);
    long stopTime = Instant.now().toEpochMilli() + duration;
    AtomicReference<String> message = new AtomicReference<>();

    LongConsumer timeCheck =
        millis -> {
          message.set("---");
          if (millis >= stopTime) {
            message.set("STOP");
          } else {
            message.set("CONTINUE");
          }
        };

    // Current time in milliseconds
    long currentTimeMillis = Instant.now().toEpochMilli();
    timeCheck.accept(currentTimeMillis);
    Assertions.assertEquals("CONTINUE", message.toString());

    long pastStopTime = currentTimeMillis + duration + 10000L;
    timeCheck.accept(pastStopTime);
    Assertions.assertEquals("STOP", message.toString());
  }

  @Test
  void doubleConsumer() {
    AtomicReference<Double> temperature = new AtomicReference<>(0.0);
    DoubleConsumer celsiusToFahrenheit = celsius -> temperature.set(celsius * 9 / 5 + 32);
    celsiusToFahrenheit.accept(100);
    Assertions.assertEquals(212.0, temperature.get());

    // radius of circles
    List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
    // calculate area of circle
    BiConsumer<Integer, DoubleConsumer> biConsumer =
        (radius, consumer) -> {
          consumer.accept(Math.PI * radius * radius);
        };
    DoubleStream result = input.stream().mapMultiToDouble(biConsumer);
    Assertions.assertArrayEquals(
        new double[] {3.14, 12.56, 28.27, 50.26, 78.53}, result.toArray(), 0.01);
  }

  @Test
  void objIntConsumer() {
    AtomicReference<String> result = new AtomicReference<>();
    ObjIntConsumer<String> trim =
        (input, len) -> {
          if (input != null && input.length() > len) {
            result.set(input.substring(0, len));
          }
        };

    trim.accept("123456789", 3);
    Assertions.assertEquals("123", result.get());
  }

  @Test
  void objLongConsumer() {
    AtomicReference<LocalDateTime> result = new AtomicReference<>();
    ObjLongConsumer<LocalDateTime> trim =
        (input, delta) -> {
          if (input != null) {
            result.set(input.plusSeconds(delta));
          }
        };

    LocalDateTime input = LocalDateTime.now().toLocalDate().atStartOfDay();
    trim.accept(input, TimeUnit.DAYS.toMillis(1));
    Assertions.assertEquals(0, result.get().getMinute());
  }

  @ParameterizedTest
  @CsvSource(
      value = {"{0};12,345.678", "{0,number,#.##};12345.68", "{0,number,currency};$12,345.68"},
      delimiter = ';')
  void objDoubleConsumer(String formatString, String expected) {
    AtomicReference<String> result = new AtomicReference<>();
    ObjDoubleConsumer<String> format =
        (formatStr, input) -> {
          result.set(MessageFormat.format(formatStr, input));
        };

    double number = 12345.678;
    format.accept(formatString, number);
    Assertions.assertEquals(expected, result.get());
  }
}
