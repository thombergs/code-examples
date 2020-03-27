package io.reflectoring.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class GitRepositoryIdWithValueOfTestController {

  @GetMapping("/repositories/{repositoryId}")
  String getRepository(@PathVariable("repositoryId") GitRepositoryIdWithValueOf gitRepositoryId) {
    assertThat(gitRepositoryId).isNotNull();
    return "test";
  }

}
