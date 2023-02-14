package source.argument.conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.temporal.ChronoUnit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.ValueSource;

public class ArgumentConversionTest {

	@ParameterizedTest
	@ValueSource(ints = { 2, 4 })
	void checkWideningArgumentConversion(long number) {
		assertEquals(0, number % 2);
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@ValueSource(strings = "DAYS")
	void checkImplicitArgumentConversion(ChronoUnit argument) {
		assertNotNull(argument.name());
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@ValueSource(strings = { "Name1", "Name2" })
	void checkImplicitFallbackArgumentConversion(Person person) {
		assertNotNull(person.getName());
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@ValueSource(ints = { 100 })
	void checkExplicitArgumentConversion(@ConvertWith(StringSimpleArgumentConverter.class) String argument) {
		assertEquals("100", argument);
	}
}
