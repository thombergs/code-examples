package io.reflectoring.records;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record Person(
    @NotBlank String name,
    @Min(value = 0, message = "Age cannot be lesser that 0")
        @Max(value = 130, message = "Age cannot be greater that 130")
        int age) {
  public Person() {
    this("Foo", 50);
  }

  public Person(String name) {
    this(name, 50);
  }

  public boolean isNameValid() {
    return !(name == null || name.isBlank());
  }

  public Person withName(String name) {
    return new Person(name, age);
  }
}
