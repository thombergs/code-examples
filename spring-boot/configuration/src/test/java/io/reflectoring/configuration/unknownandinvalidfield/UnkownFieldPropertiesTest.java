package io.reflectoring.configuration.unknownandinvalidfield;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = {
		"myapp.unknown-and-invalid-field-module.unknown-property=foo"
})
class UnkownFieldPropertiesTest {

	@Autowired(required = false)
	private UnkownAndInvalidFieldProperties properties;

	@Test
	@Disabled("disabled due to unexpected behavior")
	void loadsApplicationContext() {
		// This test passes.
		// However, I would expect this test to fail application context startup due to the unknown property.
		// If we set ignoreInvalidFields to true, it works as expected.
		assertThat(properties).isNotNull();
	}
}