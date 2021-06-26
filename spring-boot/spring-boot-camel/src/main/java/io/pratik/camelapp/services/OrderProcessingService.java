/**
 * 
 */
package io.pratik.camelapp.services;

import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pratik.camelapp.models.Order;
import io.pratik.camelapp.models.OrderLine;

/**
 * @author pratikdas
 *
 */
@Service
public class OrderProcessingService {
	
	@Autowired
	private ProducerTemplate producerTemplate;
	
	public Order process(final List<OrderLine> orderLines) {
		Order order = producerTemplate.requestBody(
                "direct:processOrder", orderLines, Order.class);
		
		return order;
	}

}
