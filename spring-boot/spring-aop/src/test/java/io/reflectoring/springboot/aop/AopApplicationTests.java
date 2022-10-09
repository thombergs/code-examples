package io.reflectoring.springboot.aop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AopApplicationTests {
	@Autowired
	ShipmentService shipmentService;

	@Autowired
	BillingService billingService;

	@Test
	void testBeforeLog() {
		shipmentService.shipStuff();
	}

	@Test
	void testBeforeLogWithBill() {
		shipmentService.shipStuffWithBill();
	}

	@Test
	void testWithin() {
		billingService.createBill();
	}

}
