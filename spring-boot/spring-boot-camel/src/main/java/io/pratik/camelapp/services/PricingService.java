/**
 * 
 */
package io.pratik.camelapp.services;

import org.springframework.stereotype.Service;

import io.pratik.camelapp.models.OrderLine;

/**
 * @author pratikdas
 *
 */
@Service
public class PricingService {
	
	public OrderLine calculatePrice(final OrderLine orderLine ) {
		String category = orderLine.getProduct().getProductCategory();
		if("Electronics".equalsIgnoreCase(category))
		   orderLine.setPrice(300.0);
		else if("Household".equalsIgnoreCase(category))
			orderLine.setPrice(55.0);
		else
			orderLine.setPrice(99.0);
		return orderLine;
		
	}

}
