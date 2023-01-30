package source.enumeration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.temporal.ChronoUnit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class EnumSourceTest {

	@ParameterizedTest
	@EnumSource(ChronoUnit.class)
	void checkEnumSourceValue(ChronoUnit unit) {
		assertNotNull(unit);
	}

	// ---------------------------------------------------------------------------

	@ParameterizedTest
	@EnumSource(names = { "DAYS", "HOURS" })
	void checkEnumSourceNames(ChronoUnit unit) {
		assertNotNull(unit);
	}
}
