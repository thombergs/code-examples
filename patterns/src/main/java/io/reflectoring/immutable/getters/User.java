package io.reflectoring.immutable.getters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
class User {

  private final Long id;

  private final List<String> roles;

}
