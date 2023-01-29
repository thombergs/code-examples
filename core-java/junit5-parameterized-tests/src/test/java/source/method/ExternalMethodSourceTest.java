package source.method;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ExternalMethodSourceTest {

	// Note: The test will try to load the external method
	@ParameterizedTest
	@MethodSource("source.method.ExternalMethodSource#checkExternalMethodSourceArgs")
	void checkExternalMethodSource(String word) {
		assertTrue(StringUtils.isAlphanumeric(word), "Supplied word is not alpha-numeric");
	}
}
