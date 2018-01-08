package com.example.demo;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "addresses", path = "/addresses_mto")
public interface AddressClient {

	@RequestMapping(method = RequestMethod.GET, path = "/")
	Resources<Address> getAddresses();

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	Resource<Address> getAddress(@PathVariable("id") long id);

	@RequestMapping(method = RequestMethod.PUT, consumes = "text/uri-list", path="/{addressId}/customer")
	Resource<Address> associateWithCustomer(@PathVariable("addressId") long addressId, @RequestBody String customerUri);

	@RequestMapping(method = RequestMethod.GET, path="/{addressId}/customer")
	Resource<Customer> getCustomer(@PathVariable("addressId") long addressId);

}
