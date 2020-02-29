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

@WebMvcTest(controllers = TestController.class)
class RepositoryArgumentResolverTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RepositoryFinder repositoryFinder;

  @Test
  void resolvesSiteSuccessfully() throws Exception {

    given(repositoryFinder.findBySlug("my-repo"))
        .willReturn(Optional.of(new Repository(1L, "my-repo")));

    mockMvc.perform(get("/my-repo/foo"))
        .andExpect(status().isOk());
  }

  @Test
  void notFoundOnUnknownSlug() throws Exception {

    given(repositoryFinder.findBySlug("unknownSlug"))
        .willReturn(Optional.empty());

    mockMvc.perform(get("/unknownSlug/foo"))
        .andExpect(status().isNotFound());
  }

}