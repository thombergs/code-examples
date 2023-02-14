package source.arguments.aggregator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;

public class ArgumentsAggregatorTest {

	@ParameterizedTest
	@CsvSource({ "John, 20", "Harry, 30" })
	void checkArgumentsAggregator(@AggregateWith(PersonArgumentsAggregator.class) Person person) {
		assertTrue(person.getAge() > 19, person.getName() + " is a teenager");
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@CsvSource({ "John, 20", "Harry, 30" })
	void checkCustomAggregatorAnnotation(@CsvToPerson Person person) {
		assertTrue(person.getAge() > 19, person.getName() + " is a teenager");
	}
}
