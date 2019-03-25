package io.reflectoring.pagination;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaginatedController.class)
class PaginatedControllerTest {

  @MockBean
  private CharacterRepository characterRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void paginationParametersAreEvaluated() throws Exception {

    mockMvc.perform(get("/characters")
            .param("page", "5")
            .param("size", "10")
            .param("sort", "id,desc") // <-- no space after comma!!!
            .param("sort", "name,asc")) // <-- no space after comma!!!
            .andExpect(status().isOk());

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(characterRepository).findAll(pageableCaptor.capture());
    PageRequest pageable = (PageRequest) pageableCaptor.getValue();
    assertThat(pageable.getPageNumber()).isEqualTo(5);
    assertThat(pageable.getPageSize()).isEqualTo(10);
    assertThat(pageable.getSort().getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
    assertThat(pageable.getSort().getOrderFor("id").getDirection()).isEqualTo(Sort.Direction.DESC);

  }

}
