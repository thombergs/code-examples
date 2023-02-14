package source.method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MethodSourceTest {

	// Note: The test will try to load the supplied method
	@ParameterizedTest
	@MethodSource("checkExplicitMethodSourceArgs")
	void checkExplicitMethodSource(String word) {
		assertTrue(StringUtils.isAlphanumeric(word), "Supplied word is not alpha-numeric");
	}

	static Stream<String> checkExplicitMethodSourceArgs() {
		return Stream.of("a1", "b2");
	}

	// ---------------------------------------------------------------------------

	// Note: The test will search for the source method that matches the test-case
	// method name
	@ParameterizedTest
	@MethodSource
	void checkImplicitMethodSource(String word) {
		assertTrue(StringUtils.isAlphanumeric(word), "Supplied word is not alpha-numeric");
	}

	static Stream<String> checkImplicitMethodSource() {
		return Stream.of("a1", "b2");
	}

	// ---------------------------------------------------------------------------

	// Note: The test will automatically map arguments based on the index
	@ParameterizedTest
	@MethodSource
	void checkMultiArgumentsMethodSource(int number, String expected) {
		assertEquals(StringUtils.equals(expected, "even") ? 0 : 1, number % 2);
	}

	static Stream<Arguments> checkMultiArgumentsMethodSource() {
		return Stream.of(Arguments.of(2, "even"), Arguments.of(3, "odd"));
	}
}
