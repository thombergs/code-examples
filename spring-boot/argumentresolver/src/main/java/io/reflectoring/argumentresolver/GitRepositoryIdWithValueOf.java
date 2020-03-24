package io.reflectoring.argumentresolver;

import lombok.Value;

@Value
class GitRepositoryIdWithValueOf {

  private final long value;

  /**
   * If we have a static valueOf(String) method, we don't need a Converter!
   */
  public static GitRepositoryIdWithValueOf valueOf(String value){
    return new GitRepositoryIdWithValueOf(Long.parseLong(value));
  }

}
