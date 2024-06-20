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

	@Autowired
	OrderService orderService;

	@Autowired
	ValidationService validationService;

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

	@Test
	void testWithArgs() {
		billingService.createBill(10L);
	}

	@Test
	void testOrderWithLogicalOperator() {
		orderService.orderStuff();
	}

	@Test
	void testCancelWithLogicalOperator() {
		orderService.cancelStuff();
	}

	@Test
	void testCheckingStuffWithAfter() {
		orderService.checkStuff();
	}

	@Test
	void testValidAroundAspect() {
		validationService.validateNumber(10);
	}

	@Test
	void testInvalidAroundAspect() {
		validationService.validateNumber(-4);
	}
}
