package io.reflectoring.records;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

@Slf4j
class PersonTest {

  @Test
  void allArgConstructor() {
    final Person bar = new Person("Bar", 40);
    assertThat(bar)
        .as("Person cannot be null.")
        .isNotNull()
        .hasToString("Person[name=Bar, age=40]");
    log.info("Bar {}", bar);
  }

  @Test
  void compactConstructor() {
    final Person person = new Person();
    assertThat(person)
        .as("Person cannot be null.")
        .isNotNull()
        .hasToString("Person[name=Foo, age=50]");
    log.info("Person {}", person);
  }

  @Test
  void nameArgConstructor() {
    final Person person = new Person("Tom");
    assertThat(person)
        .as("Person cannot be null.")
        .isNotNull()
        .hasToString("Person[name=Tom, age=50]");
    log.info("Person {}", person);
  }

  @Test
  void name() {
    final Person person = new Person();
    assertThat(person).as("Person cannot be null.").isNotNull().extracting("name").isEqualTo("Foo");
    log.info("Person {}", person);
  }

  @Test
  void age() {
    final Person person = new Person();
    assertThat(person).as("Person cannot be null.").isNotNull().extracting("age").isEqualTo(50);
    log.info("Person {}", person);
  }

  @Test
  void adult() {
    final Person adultPerson = new Person("Tom", 20);
    assertThat(adultPerson.isAdult()).as("Person should be adult.").isTrue();
    log.info("Adult Person {}", adultPerson);

    final Person minorPerson = new Person("Jerry", 10);
    assertThat(minorPerson.isAdult()).as("Person should not be adult.").isFalse();
    log.info("Minor Person {}", minorPerson);
  }

  @Test
  void helperMethods() {
    final Person fooPerson = new Person("Foo", 40);
    final Person barPerson = new Person("Bar", 40);
    SoftAssertions personBundle = new SoftAssertions();
    personBundle.assertThat(fooPerson).isNotEqualTo(barPerson);
    personBundle.assertThat(fooPerson.toString()).isNotEmpty();
    personBundle.assertThat(fooPerson.hashCode()).isPositive();
    personBundle.assertThat(barPerson.toString()).isNotEmpty();
    personBundle.assertThat(barPerson.hashCode()).isPositive();
    personBundle.assertAll();

    log.info("Foo Person toString {} with hashcode {}", fooPerson, fooPerson.hashCode());
    log.info("Bar Person toString {} with hashcode {}", barPerson, barPerson.hashCode());
  }

  @Test
  void delegateMethod() {
    final Person person = new Person();
    final Person newPerson = person.withName("Tom");
    assertThat(newPerson)
        .as("Person cannot be null.")
        .isNotNull()
        .extracting("name", "age")
        .containsExactly("Tom", 50);
    log.info("Person {}", newPerson);
  }

  @Test
  void invalidAge() {
    final Throwable thrown = catchThrowable(() -> new Person("Bob", 132));
    assertThat(thrown)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Got invalid age");
  }
}
