package io.reflectoring.immutable.builder;

import lombok.Builder;

@Builder
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

}
