package io.reflectoring.argumentresolver;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class RepositoryIdConverter implements Converter<String, RepositoryId> {

  @Override
  public RepositoryId convert(String source) {
    return new RepositoryId(Long.parseLong(source));
  }
}
