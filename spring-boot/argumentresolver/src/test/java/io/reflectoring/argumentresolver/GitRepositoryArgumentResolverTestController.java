package io.reflectoring.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/{repositorySlug}")
class GitRepositoryArgumentResolverTestController {

  @GetMapping("/contributors")
  String listContributors(GitRepository gitRepository) {
    assertThat(gitRepository.getId()).isEqualTo(1L);
    return "test";
  }

}
