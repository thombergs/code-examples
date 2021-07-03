/**
 * 
 */
package io.pratik.camelapp.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.pratik.camelapp.services.PriceAggregationStrategy;

/**
 * @author pratikdas
 *
 */
@Component
public class OrderProcessingRoute   extends RouteBuilder {
	
	@Autowired
	private PriceAggregationStrategy priceAggregationStrategy;

	@Override
	public void configure() throws Exception {
		from("direct:fetchProcess")
		.split(body(), priceAggregationStrategy).parallelProcessing()
		.to("bean:pricingService?method=calculatePrice")
		.end();
	}

}
