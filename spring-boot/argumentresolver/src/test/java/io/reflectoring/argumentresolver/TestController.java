package io.reflectoring.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestController {

  @GetMapping("/{slug}/foo")
  String getSomething(Repository repository) {
    assertThat(repository.getId()).isEqualTo(1L);
    return "test";
  }

}
