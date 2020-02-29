package io.reflectoring.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/{repositorySlug}")
class RepositoryArgumentResolverTestController {

  @GetMapping("/listContributors")
  String listContributors(Repository repository) {
    assertThat(repository.getId()).isEqualTo(1L);
    return "test";
  }

}
