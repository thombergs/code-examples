package io.reflectoring.springboot.testconfiguration;

import io.reflectoring.springboot.testconfiguration.service.DataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Import(WebClientTestConfiguration.class)
@TestPropertySource(locations="classpath:test.properties")
class TestConfigurationExampleAppTests {
	@Autowired
	private DataService dataService;

	@Test
	void contextLoads() {
	}
}
