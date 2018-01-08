package com.example.demo;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "customers", path = "/customers_mto")
public interface CustomerClient {

	@RequestMapping(method = RequestMethod.GET, value = "/")
	Resources<Customer> getCustomers();

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	Resource<Customer> getCustomer(@PathVariable("id") long id);

}
