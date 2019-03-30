package io.reflectoring.paging;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static io.reflectoring.paging.PageableAssert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PagedController.class)
@TestPropertySource(properties = {
		"spring.data.web.pageable.prefix=prefix_",
		"spring.data.web.pageable.size-parameter=my-size",
		"spring.data.web.pageable.page-parameter=my-page"
})
class PagedControllerWithCustomPagingPropertiesTest {

	@MockBean
	private MovieCharacterRepository characterRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void evaluatesPageableParameter() throws Exception {

		mockMvc.perform(get("/characters/page")
				.param("prefix_my-page", "5")
				.param("prefix_my-size", "10")
				.param("sort", "id,desc") // <-- no space after comma!!!
				.param("sort", "name,asc")) // <-- no space after comma!!!
				.andExpect(status().isOk());

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(characterRepository).findAllPage(pageableCaptor.capture());
		PageRequest pageable = (PageRequest) pageableCaptor.getValue();

		assertThat(pageable).hasPageNumber(5);
		assertThat(pageable).hasPageSize(10);
		assertThat(pageable).hasSort("name", Sort.Direction.ASC);
		assertThat(pageable).hasSort("id", Sort.Direction.DESC);
	}

}
