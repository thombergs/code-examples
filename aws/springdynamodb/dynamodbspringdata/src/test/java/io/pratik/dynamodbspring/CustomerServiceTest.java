/**
 * 
 */
package io.pratik.dynamodbspring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.pratik.dynamodbspring.models.Customer;

/**
 * @author pratikdas
 *
 */
@SpringBootTest
class CustomerServiceTest {

	@Autowired
	private CustomerService customerService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCreateCustomer() {
		Customer customer = new Customer();		
		customer.setCustomerID("CUST-001");
		customer.setName("pratik");
		customer.setEmail("hgjgjh");
		customerService.createCustomer(customer);
	}


}
