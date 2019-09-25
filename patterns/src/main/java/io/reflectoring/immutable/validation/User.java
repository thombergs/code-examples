package io.reflectoring.immutable.validation;

import java.util.Optional;

class User {

  private final Long id;

  private final String name;

  User(Long id, String name) {
    if( id < 0 ){
      throw new IllegalArgumentException("id must be >= 0!");
    }

    if(name == null || "".equals(name)){
      throw new IllegalArgumentException("name must not be null or empty!");
    }

    this.id = id;
    this.name = name;
  }

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
