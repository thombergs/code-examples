/**
 * 
 */
package io.pratik.jobscheduling.jobs;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

/**
 * @author pratikdas
 *
 */
@Service
public class PricingEngine {
	
	static final Logger LOGGER = Logger.getLogger(PricingEngine.class.getName());
	private Double price;
	
	public Double getProductPrice() {
		return price;
		
	}
	
	//@Scheduled(fixedDelayString = "${interval}")
	//@Scheduled(cron = "@hourly")
	@Scheduled(cron = "${interval-in-cron}")
	@SchedulerLock(name = "scheduledTaskName")
	public void computePrice() throws InterruptedException {
		
		Random random = new Random();
		price = random.nextDouble() * 100;
		LOGGER.info("computing price at "+ LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));	
		Thread.sleep(4000);
	}
	
	//@Scheduled(fixedRateString = "${interval}")
	@Scheduled(initialDelay = 2000, fixedRate = 3000)
	@Async
	public void refreshPricingParameters() {
		
		// update pricing parameters
		LOGGER.info("computing price at "+ LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));	
	}

}
