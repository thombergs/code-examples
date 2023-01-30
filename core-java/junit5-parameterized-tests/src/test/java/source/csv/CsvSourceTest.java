package source.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CsvSourceTest {

	@ParameterizedTest
	@CsvSource({ "2, even", 
				 "3, odd"})
	void checkCsvSource(int number, String expected) {
		assertEquals(StringUtils.equals(expected, "even") ? 0 : 1, number % 2);
	}
}
