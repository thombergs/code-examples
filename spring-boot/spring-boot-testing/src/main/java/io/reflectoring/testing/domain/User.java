package io.reflectoring.testing.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

  private Long id;
  private String name;
  private String email;
  private LocalDateTime registrationDate;

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }
}
