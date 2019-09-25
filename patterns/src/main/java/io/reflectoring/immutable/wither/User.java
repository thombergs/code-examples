package io.reflectoring.immutable.wither;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class User {

  private final Long id;

  private final String name;

  User withId(Long id) {
    return new User(id, this.name);
  }

  User withName(String name) {
    return new User(this.id, name);
  }

}
