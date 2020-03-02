package io.reflectoring.argumentresolver;

import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
class RepositoryArgumentResolver implements HandlerMethodArgumentResolver {

  private final RepositoryFinder repositoryFinder;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameter().getType() == Repository.class;
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {

    String requestPath = ((ServletWebRequest) webRequest).getRequest().getPathInfo();

    String slug = requestPath
        .substring(0, requestPath.indexOf("/", 1))
        .replaceAll("^/", "");

    Optional<Repository> repository = repositoryFinder.findBySlug(slug);

    if (repository.isEmpty()) {
      throw new NotFoundException();
    }

    return repository.get();
  }
}
