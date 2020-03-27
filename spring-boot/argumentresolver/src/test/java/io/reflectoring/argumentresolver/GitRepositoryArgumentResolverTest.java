package io.reflectoring.argumentresolver;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = GitRepositoryArgumentResolverTestController.class)
class GitRepositoryArgumentResolverTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GitRepositoryFinder gitRepositoryFinder;

  @Test
  void resolvesSiteSuccessfully() throws Exception {

    given(gitRepositoryFinder.findBySlug("my-repo"))
        .willReturn(Optional.of(new GitRepository(1L, "my-repo")));

    mockMvc.perform(get("/my-repo/contributors"))
        .andExpect(status().isOk());
  }

  @Test
  void notFoundOnUnknownSlug() throws Exception {

    given(gitRepositoryFinder.findBySlug("unknownSlug"))
        .willReturn(Optional.empty());

    mockMvc.perform(get("/unknownSlug/contributors"))
        .andExpect(status().isNotFound());
  }

}