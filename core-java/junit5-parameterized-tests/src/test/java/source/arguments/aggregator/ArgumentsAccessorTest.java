package source.arguments.aggregator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

public class ArgumentsAccessorTest {

	@ParameterizedTest
	@CsvSource({ "John, 20", 
				 "Harry, 30" })
	void checkArgumentsAccessor(ArgumentsAccessor arguments) {
		Person person = new Person(arguments.getString(0), arguments.getInteger(1));
		assertTrue(person.getAge() > 19, person.getName() + " is a teenager");
	}
}
