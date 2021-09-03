/**
 * 
 */
package io.pratik.metricscapture;

import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author pratikdas
 *
 */
@Service
public class PricingEngine {
	
	private Double price;
	
	public Double getProductPrice() {
		return price;
		
	}
	
	@Scheduled(fixedRate = 70000)
	public void computePrice() {
		
		Random random = new Random();
		price = random.nextDouble() * 100;
		System.out.println("computing price "+price);
		
	}

}
