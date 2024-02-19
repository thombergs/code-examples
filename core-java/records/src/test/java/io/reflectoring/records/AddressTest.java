package io.reflectoring.records;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class AddressTest {
  @Test
  void immutableCities() {
    List<String> streets = new ArrayList<>();
    streets.add("Street 1");
    streets.add("Street 2");
    Condition<String> stringCondition = new Condition<>(streets::contains, "Street 3");

    Address address = new Address("City", List.copyOf(streets));
    streets.add("Street 3");

    log.info("Address {}", address);
    assertThat(address)
        .isNotNull()
        .extracting(Address::streets)
        .matches(
            (Predicate<? super List<String>>)
                list -> list.containsAll(Arrays.asList("Street 1", "Street 2")),
            "Should have Street 1 and 2");
  }
}
