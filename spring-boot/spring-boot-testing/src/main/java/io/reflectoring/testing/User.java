package io.reflectoring.testing;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class User {

  @Id
  private Long id;
  private String name;
  private String email;
  private LocalDateTime registrationDate;

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }
}
