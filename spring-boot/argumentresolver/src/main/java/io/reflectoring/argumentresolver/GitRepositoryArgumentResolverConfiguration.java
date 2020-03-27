package io.reflectoring.argumentresolver;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
class GitRepositoryArgumentResolverConfiguration implements WebMvcConfigurer {

  private final GitRepositoryFinder gitRepositoryFinder;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new GitRepositoryArgumentResolver(gitRepositoryFinder));
  }

}
