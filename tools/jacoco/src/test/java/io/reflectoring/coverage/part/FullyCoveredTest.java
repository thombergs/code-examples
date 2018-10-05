package io.reflectoring.coverage.part;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class FullyCoveredTest {

	@Test
	void test() {
		FullyCovered fullyCovered = new FullyCovered();
		String string = fullyCovered.lowercase("THIS IS A STRING");
		assertThat(string).isEqualTo("this is a string");
	}

}