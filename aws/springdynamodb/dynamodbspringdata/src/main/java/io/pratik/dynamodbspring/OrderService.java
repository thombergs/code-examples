/**
 * 
 */
package io.pratik.dynamodbspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pratik.dynamodbspring.models.Order;
import io.pratik.dynamodbspring.repositories.OrderRepository;

/**
 * @author pratikdas
 *
 */
@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	public void createOrder(final Order order) {
		orderRepository.save(order);
	}

}
