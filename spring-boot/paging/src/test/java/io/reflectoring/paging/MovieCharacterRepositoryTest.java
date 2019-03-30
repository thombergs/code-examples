package io.reflectoring.paging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MovieCharacterRepositoryTest {

	@Autowired
	private MovieCharacterRepository characterRepository;

	@Test
	void returnsPage() {
		// database is initialized with script "data.sql"
		assertThat(
				characterRepository
						.findAllPage(PageRequest.of(0, 10))
						.getContent()
						.size())
				.isEqualTo(10);

	}

	@Test
	void returnsSlice() {
		// database is initialized with script "data.sql"
		assertThat(
				characterRepository
						.findAllSlice(PageRequest.of(0, 10))
						.getContent()
						.size())
				.isEqualTo(10);

	}

}