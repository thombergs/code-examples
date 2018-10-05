package io.reflectoring.coverage.part;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class PartlyCoveredTest {

	@Test
	void test() {
		PartlyCovered partlyCovered = new PartlyCovered();
		String string = partlyCovered.partlyCovered("THIS IS A STRING", false);
		assertThat(string).isEqualTo("THIS IS A STRING");
	}

	@Test
	void testSimple() {
		PartlyCovered fullyCovered = new PartlyCovered();
		String string = fullyCovered.covered("THIS IS A STRING");
		assertThat(string).isEqualTo("this is a string");
	}

}