package source.csv.file;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class CsvFileSourceTest {

	@ParameterizedTest
	@CsvFileSource(files = "src/test/resources/csv-file-source.csv", numLinesToSkip = 1)
	void checkCsvFileSource(int number, String expected) {
		assertEquals(StringUtils.equals(expected, "even") ? 0 : 1, number % 2);
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@CsvFileSource(files = "src/test/resources/csv-file-source_attributes.csv", 
				   delimiterString = "|",
				   lineSeparator = "||",
				   numLinesToSkip = 1)
	void checkCsvFileSourceAttributes(int number, String expected) {
		assertEquals(StringUtils.equals(expected, "even") ? 0 : 1, number % 2);
	}
}
