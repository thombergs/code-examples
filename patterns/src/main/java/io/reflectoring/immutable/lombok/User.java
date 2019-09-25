package io.reflectoring.immutable.lombok;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class User {

  private final Long id;

  private final String name;

}
