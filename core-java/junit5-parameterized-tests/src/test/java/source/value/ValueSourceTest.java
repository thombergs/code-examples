package source.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ValueSourceTest {

	@ParameterizedTest
	@ValueSource(ints = { 2, 4 })
	void checkEvenNumber(int number) {
		assertEquals(0, number % 2, "Supplied number is not an even number");
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@ValueSource(strings = { "a1", "b2" })
	void checkAlphanumeric(String word) {
		assertTrue(StringUtils.isAlphanumeric(word), "Supplied word is not alpha-numeric");
	}
}
