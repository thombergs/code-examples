/**
 * 
 */
package io.pratik.dynamodbapps;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.pratik.dynamodbapps.models.Order;
import io.pratik.dynamodbapps.models.Product;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

/**
 * @author pratikdas
 *
 */
@SpringBootTest
class OrderRepositoryTest {
	
	@Autowired
	private OrderRepository orderRepository;

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
	void testCreateOrder() {
		Order order = new Order();
		order.setOrderID("ORD-010");
		order.setCustomerID("CUST-001");
		
		List<Product> products = new ArrayList<Product>();
		
		Product product = new Product();
		product.setName("Television");
		product.setBrand("samsung");
		product.setPrice(112.56);
		products.add(product);
		
		product = new Product();
		product.setName("Washing Machine");
		product.setBrand("panasonic");
		product.setPrice(119.99);
		products.add(product);
		
		order.setProducts(products );
		order.setOrderValue(56.7);
		order.setCreatedDate(Instant.now());
		orderRepository.save(order);
	}
	
	@Test
	void testGetOrder() {
		Order order = 
		    orderRepository.getOrder("CUST-001", "ORD-010");
		System.out.println("order "+order.getProducts());
	}

	@Test
	void testFindOrdersByValue() {
		PageIterable<Order> orders = 
		    orderRepository.findOrdersByValue("CUST-001", 5.0d);
		System.out.println("orders "+orders.items());
	}
}
