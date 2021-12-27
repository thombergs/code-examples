package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  private CustomerClient customerClient;

  private AddressClient addressClient;

  private Logger logger = LoggerFactory.getLogger(Controller.class);

  @Autowired
  public Controller(CustomerClient customerClient, AddressClient addressClient) {
    this.customerClient = customerClient;
    this.addressClient = addressClient;
  }

  @GetMapping(path = "customers/{id}")
  public CustomerAndAddress getCustomerWithAddress(@PathVariable("id") long customerId){
    logger.info("COLLECTING CUSTOMER AND ADDRESS WITH ID {} FROM UPSTREAM SERVICE", customerId);
    Customer customer = customerClient.getCustomer(customerId);
    Address address = addressClient.getAddressForCustomerId(customerId);
    return new CustomerAndAddress(customer, address);
  }

}
