package io.reflectoring.records;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;

@Slf4j
class PersonTest {
  private static final Validator VALIDATOR =
      Validation.byDefaultProvider()
          .configure()
          .messageInterpolator(new ParameterMessageInterpolator())
          .buildValidatorFactory()
          .getValidator();

  @Test
  void allArgConstructor() {
    Person bob = new Person("Bob", 40);
    assertThat(bob)
        .as("Person cannot be null.")
        .isNotNull()
        .hasToString("Person[name=Bob, age=40]");
    log.info("Bob {}", bob);
  }

  @Test
  void compactConstructor() {
    Person person = new Person();
    assertThat(person)
        .as("Person cannot be null.")
        .isNotNull()
        .hasToString("Person[name=Foo, age=50]");
    log.info("Person {}", person);
  }

  @Test
  void nameArgConstructor() {
    Person person = new Person("Tom");
    assertThat(person)
        .as("Person cannot be null.")
        .isNotNull()
        .hasToString("Person[name=Tom, age=50]");
    log.info("Person {}", person);
  }

  @Test
  void name() {
    Person person = new Person();
    assertThat(person).as("Person cannot be null.").isNotNull().extracting("name").isEqualTo("Foo");
    log.info("Person {}", person);
  }

  @Test
  void age() {
    Person person = new Person();
    assertThat(person).as("Person cannot be null.").isNotNull().extracting("age").isEqualTo(50);
    log.info("Person {}", person);
  }

  @Test
  void helperMethods() {
    Person fooPerson = new Person("Foo", 40);
    Person barPerson = new Person("Bar", 40);
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
    Person person = new Person();
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
    final Person invalidPerson = new Person("Bob", 132);

    final Set<ConstraintViolation<Person>> constraintViolations = VALIDATOR.validate(invalidPerson);

    log.info("Got {} constraint violations: {}", constraintViolations.size(), constraintViolations);
    assertThat(constraintViolations)
        .filteredOn(
            personConstraintViolation ->
                personConstraintViolation.getMessage().equals("Age cannot be greater that 130"))
        .hasSize(1);
  }

  @Test
  void finalRecord() {
    Person newPerson = new Person("Bob", 50);
    newPerson = new Person("Tom", 40);

    log.info("Person {}", newPerson);
  }
}
