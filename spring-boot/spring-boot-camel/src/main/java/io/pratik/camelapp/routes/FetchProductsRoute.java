/**
 * 
 */
package io.pratik.camelapp.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import io.pratik.camelapp.services.ProductService;

/**
 * @author pratikdas
 *
 */
@Component
public class FetchProductsRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:fetchProducts")
		  .routeId("direct-fetchProducts")
		  .tracing()
		  .log(">>> ${body}")
		  .bean(ProductService.class, "fetchProductsByCategory")
		  .end();
	}

}
