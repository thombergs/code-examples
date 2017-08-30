package com.example.demo;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "addresses", path = "/addresses")
public interface AddressClient {

  @RequestMapping(method = RequestMethod.GET, path = "/{id}")
  Address getAddressForCustomerId(@PathVariable("id") long id);

}
