package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;

/**
 * For this application to work,  the "spring-data-rest-associations" application must be running.
 */
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringBootApplication
@EnableFeignClients
public class DemoApplication implements ApplicationRunner{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	private Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	private AddressClient addressClient;

	@Autowired
	public DemoApplication(AddressClient addressClient) {
		this.addressClient = addressClient;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info(addressClient.getAddresses().toString());
		logger.info(addressClient.getAddress(1L).toString());
		addressClient.associateWithCustomer(2L, "http://localhost:8080/customers_mto/1");
		logger.info(addressClient.getCustomer(2L).toString());
	}
}
