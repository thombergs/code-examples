package source.null_empty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class NullEmptySourceTest {

	@ParameterizedTest
	@NullSource
	void checkNull(String value) {
		assertEquals(null, value);
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@EmptySource
	void checkEmpty(String value) {
		assertEquals("", value);
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@NullAndEmptySource
	void checkNullAndEmpty(String value) {
		assertTrue(value == null || value.isEmpty());
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { " ", "   " })
	void checkNullEmptyAndBlank(String value) {
		assertTrue(value == null || value.isBlank());
	}
}
