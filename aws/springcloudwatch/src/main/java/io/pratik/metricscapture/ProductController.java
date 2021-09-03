/**
 * 
 */
package io.pratik.metricscapture;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.pratik.metricscapture.models.Order;
import io.pratik.metricscapture.models.Product;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pratikdas
 *
 */
@RestController
@Slf4j
public class ProductController {
	private Counter pageViewsCounter;
	private Timer productTimer;
	private Gauge priceGauge;
	
	private MeterRegistry meterRegistry;
	
	private PricingEngine pricingEngine;

	@Autowired
	ProductController(MeterRegistry meterRegistry, PricingEngine pricingEngine){
		
		 this.meterRegistry = meterRegistry;
		 this.pricingEngine = pricingEngine;
				 
		 priceGauge = Gauge
				    .builder("product.price", pricingEngine , 
							 (pe)->{return pe != null? pe.getProductPrice() : null;})
				    .description("Product price")
				    .baseUnit("ms")
				    .register(meterRegistry);
			
		 pageViewsCounter = meterRegistry
				 .counter("PAGE_VIEWS.ProductList");
		 
		 productTimer = meterRegistry
				 .timer("execution.time.fetchProducts");
	
	}
	
	
	
	
	@GetMapping("/products")
	@ResponseBody
	public List<Product> fetchProducts() {
		long startTime = System.currentTimeMillis();
		
		List<Product> products = fetchProductsFromStore();
		
		// increment page views counter
		pageViewsCounter.increment();

		// record time to execute the method
		System.out.println("productTimer "+productTimer + " " + pageViewsCounter + " " + priceGauge);
	    productTimer.record(Duration.ofMillis(System.currentTimeMillis() - startTime));
	    	
		return products;
	}
	
	private List<Product> fetchProductsFromStore(){
		List<Product> products = new ArrayList<Product>();
		products.add(Product.builder().name("Television").build());
		products.add(Product.builder().name("Book").build());
		return products;
	}
	


}
