package source.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class ArgumentsSourceTest {

	@ParameterizedTest
	@ArgumentsSource(ExternalArgumentsProvider.class)
	void checkExternalArgumentsSource(int number, String expected) {
		assertEquals(StringUtils.equals(expected, "even") ? 0 : 1, number % 2,
				"Supplied number " + number + " is not an " + expected + " number");
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@ArgumentsSource(NestedArgumentsProvider.class)
	void checkNestedArgumentsSource(int number, String expected) {
		assertEquals(StringUtils.equals(expected, "even") ? 0 : 1, number % 2,
				"Supplied number " + number + " is not an " + expected + " number");
	}

	static class NestedArgumentsProvider implements ArgumentsProvider {

		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
			return Stream.of(Arguments.of(2, "even"), Arguments.of(3, "odd"));
		}
	}
}
