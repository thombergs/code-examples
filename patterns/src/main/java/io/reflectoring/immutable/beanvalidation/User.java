package io.reflectoring.immutable.beanvalidation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

class User extends SelfValidating<User>{

  @Min(0)
  private final Long id;

  @NotEmpty
  private final String name;

  User(Long id, String name) {
    this.id = id;
    this.name = name;
    this.validateSelf();
  }

}
