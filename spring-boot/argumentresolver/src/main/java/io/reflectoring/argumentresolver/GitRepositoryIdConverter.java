package io.reflectoring.argumentresolver;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class GitRepositoryIdConverter implements Converter<String, GitRepositoryId> {

  @Override
  public GitRepositoryId convert(String source) {
    return new GitRepositoryId(Long.parseLong(source));
  }
}
