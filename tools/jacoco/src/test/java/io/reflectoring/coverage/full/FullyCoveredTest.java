package io.reflectoring.coverage.full;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class FullyCoveredTest {

	@Test
	void test() {
		FullyCovered fullyCovered = new FullyCovered();
		String string = fullyCovered.lowercase("THIS IS A STRING");
		assertThat(string).isEqualTo("this is a string");
	}

}