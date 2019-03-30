package io.reflectoring.configuration.mail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = {
		"myapp.mail.defaultSubject="
})
@Disabled("this test will fail due to invalid properties")
class MailModuleTestWithInvalidProperties {

	@Autowired(required = false)
	private MailModuleProperties mailModuleProperties;

	@Test
	void propertiesAreLoaded() {
		assertThat(mailModuleProperties).isNotNull();
	}
}