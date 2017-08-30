package com.example.demo;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "customers", path = "/customers")
public interface CustomerClient {

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	Customer getCustomer(@PathVariable("id") long id);

}
