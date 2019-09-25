package io.reflectoring.immutable.factorymethods;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

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

}
