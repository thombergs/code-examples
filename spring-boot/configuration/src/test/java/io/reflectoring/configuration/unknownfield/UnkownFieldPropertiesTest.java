package io.reflectoring.configuration.unknownfield;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = {
		"myapp.unknownfield.unknown-property=foo"
})
@Disabled("this test is expected to fail due an unknown property")
class UnkownFieldPropertiesTest {

	@Autowired(required = false)
	private UnkownFieldProperties properties;

	@Test
	void propertiesAreLoaded() {
		assertThat(properties).isNotNull();
	}
}