package io.reflectoring.immutable.optional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class User {

  private final Long id;

  private final String name;

  static User existingUser(Long id, String name){
    return new User(id, name);
  }

  static User newUser(String name){
    return new User(null, name);
  }

  Optional<Long> getId() {
    return Optional.ofNullable(id);
  }
}
