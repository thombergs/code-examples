package io.reflectoring.paging;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import static io.reflectoring.paging.PageableAssert.*;
import static io.reflectoring.paging.SortAssert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
		controllers = PagedController.class
		// If we exclude SpringDataWebAutoConfiguration, the Pageable parameter will not be resolved.
		// If we only exclude it on the Application class itself, the test will still work!!!
		// excludeAutoConfiguration = SpringDataWebAutoConfiguration.class
)
class PagedControllerTest {

	@MockBean
	private MovieCharacterRepository characterRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void evaluatesPageableParameter() throws Exception {

		mockMvc.perform(get("/characters/page")
				.param("page", "5")
				.param("size", "10")
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

	@Test
	void evaluatesQualifier() throws Exception {

		mockMvc.perform(get("/characters/qualifier")
				.param("my_page", "5")
				.param("my_size", "10")
				.param("my_sort", "id,desc") // <-- no space after comma!!!
				.param("my_sort", "name,asc")) // <-- no space after comma!!!
				.andExpect(status().isOk());

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(characterRepository).findAllPage(pageableCaptor.capture());
		PageRequest pageable = (PageRequest) pageableCaptor.getValue();

		assertThat(pageable).hasPageNumber(5);
		assertThat(pageable).hasPageSize(10);
		assertThat(pageable).hasSort("name", Sort.Direction.ASC);
		assertThat(pageable).hasSort("id", Sort.Direction.DESC);
	}

	@Test
	void setsUpperPageLimit() throws Exception {

		mockMvc.perform(get("/characters/page")
				.param("size", "10000"))
				.andExpect(status().isOk());

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(characterRepository).findAllPage(pageableCaptor.capture());
		PageRequest pageable = (PageRequest) pageableCaptor.getValue();

		assertThat(pageable).hasPageSize(2000);
	}

	@Test
	void evaluatesPageableDefault() throws Exception {

		mockMvc.perform(get("/characters/page"))
				.andExpect(status().isOk());

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(characterRepository).findAllPage(pageableCaptor.capture());
		PageRequest pageable = (PageRequest) pageableCaptor.getValue();

		assertThat(pageable).hasPageNumber(0);
		assertThat(pageable).hasPageSize(20);
		assertThat(pageable).hasSort("name", Sort.Direction.DESC);
		assertThat(pageable).hasSort("id", Sort.Direction.ASC);
	}

	@Test
	void returnsSlice() throws Exception {

		mockMvc.perform(get("/characters/slice")
				.param("page", "5")
				.param("size", "10")
				.param("sort", "id,desc") // <-- no space after comma!!!
				.param("sort", "name,asc")) // <-- no space after comma!!!
				.andExpect(status().isOk());

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(characterRepository).findAllSlice(pageableCaptor.capture());
		PageRequest pageable = (PageRequest) pageableCaptor.getValue();

		assertThat(pageable).hasPageNumber(5);
		assertThat(pageable).hasPageSize(10);
		assertThat(pageable).hasSort("name", Sort.Direction.ASC);
		assertThat(pageable).hasSort("id", Sort.Direction.DESC);

	}

	@Test
	void evaluatesSortParameter() throws Exception {

		mockMvc.perform(get("/characters/sorted")
				.param("sort", "id,desc") // <-- no space after comma!!!
				.param("sort", "name,asc")) // <-- no space after comma!!!
				.andExpect(status().isOk());

		ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
		verify(characterRepository).findAllSorted(sortCaptor.capture());
		Sort sort = sortCaptor.getValue();

		assertThat(sort).hasSort("name", Sort.Direction.ASC);
		assertThat(sort).hasSort("id", Sort.Direction.DESC);

	}

}
