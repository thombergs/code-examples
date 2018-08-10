package io.reflectoring.springbootlogging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties =
				"spring.profiles.active=dev"
)
class LoggingWithDevProfileTest {

	private Logger logger = LoggerFactory.getLogger(LoggingWithDevProfileTest.class);

	@Test
	void test() {
		logger.info("This is a test");
	}

}
