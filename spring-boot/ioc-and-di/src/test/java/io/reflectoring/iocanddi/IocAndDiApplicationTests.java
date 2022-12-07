package io.reflectoring.iocanddi;

import io.reflectoring.iocanddi.withSpringDependencyInjection.ShippingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class IocAndDiApplicationTests {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ShippingService shippingService;

	@Test
	void contextLoads() {
		assertNotNull(restTemplate.getUriTemplateHandler());
		shippingService.ship("te");
	}

}
