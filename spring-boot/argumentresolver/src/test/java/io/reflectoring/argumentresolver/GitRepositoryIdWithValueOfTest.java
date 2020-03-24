package io.reflectoring.argumentresolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = GitRepositoryIdWithValueOfTestController.class)
class GitRepositoryIdWithValueOfTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GitRepositoryFinder gitRepositoryFinder;

  @Test
  void resolvesRepositoryId() throws Exception {
    mockMvc.perform(get("/repositories/42"))
        .andExpect(status().isOk());
  }

}