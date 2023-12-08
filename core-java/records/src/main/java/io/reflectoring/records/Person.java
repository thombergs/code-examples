package io.reflectoring.records;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record Person(
    @NotBlank String name,
    @Min(value = 0, message = "Age cannot be lesser that 0")
        @Max(value = 130, message = "Age cannot be greater that 130")
        int age) {

  private static final String DEFAULT_NAME = "Foo";
  private static final int DEFAULT_AGE = 50;
  private static final int MIN_AGE = 0;
  private static final int ADULT_MIN_AGE = 18;
  private static final int MAX_AGE = 130;

  public Person {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Got invalid name.");
    }
    if (age < MIN_AGE || age > MAX_AGE) {
      throw new IllegalArgumentException("Got invalid age");
    }
  }

  public Person() {
    this(DEFAULT_NAME, DEFAULT_AGE);
  }

  public Person(String name) {
    this(name, DEFAULT_AGE);
  }

  public boolean isNameValid() {
    return !(name == null || name.isBlank());
  }

  public boolean isAgeValid() {
    return age > MIN_AGE && age < MAX_AGE;
  }

  public boolean isAdult() {
    return age >= ADULT_MIN_AGE;
  }

  public Person withInput(String name, int age) {
    return new Person(name, age);
  }

  public Person withName(String name) {
    return new Person(name, DEFAULT_AGE);
  }

  public Person withAge(int age) {
    return new Person(DEFAULT_NAME, age);
  }
}
