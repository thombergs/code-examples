/**
 * 
 */
package io.pratik.camelapp.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.pratik.camelapp.models.Order;
import io.pratik.camelapp.services.OrderService;

/**
 * @author pratikdas
 *
 */
@Component
public class RestApiRoute  extends RouteBuilder {
	
	@Autowired
	private Environment env;

	@Override
	public void configure() throws Exception {
		CamelContext context = new DefaultCamelContext();
		
		restConfiguration()
        .contextPath("/ecommapp")
        .apiContextPath("/api-doc")
        .apiProperty("api.title", "JAVA DEV JOURNAL REST API")
        .apiProperty("api.version", "1.0")
        .apiProperty("cors", "true")
        .apiContextRouteId("doc-api")
        .port(env.getProperty("server.port", "8080"))
        .bindingMode(RestBindingMode.json);
		
		rest("/order/process")
		.get("/").description("Process order")
		.route().routeId("orders-api")
		.bean(OrderService.class, "generateOrder")
		.to("direct:fetchProcess")
		.endRest();
		
		/*
		 * rest("/orders/") .id("api-route") .consumes("application/json")
		 * .post("/bean") .bindingMode(RestBindingMode.json_xml) .type(Order.class)
		 * .to("direct:remoteService");
		 */
		
	}

}
