package io.reflectoring.argumentresolver;

import java.util.Optional;
import java.util.regex.Matcher;
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

  private static final Pattern SLUG_PATTERN = Pattern.compile("^/([^/]*).*$");

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

    Matcher matcher = SLUG_PATTERN.matcher(requestPath);

    if (!matcher.matches()) {
      throw new IllegalArgumentException(String.format(
          "Cannot resolve argument of type Site. Expecting the slug to be the first part of the request path (%s).",
          requestPath));
    }

    String slug = matcher.group(1);
    if (slug == null || slug.isBlank()) {
      throw new IllegalArgumentException(String.format(
          "Cannot resolve argument of type Site. Slug is empty (request path: %s).",
          requestPath));
    }

    Optional<Repository> repository = repositoryFinder.findBySlug(slug);

    if (repository.isEmpty()) {
      throw new NotFoundException();
    }

    return repository.get();
  }
}
