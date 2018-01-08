package com.example.demo.bidirectional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "customers_bi")
public interface BidirectionalCustomerRepository extends CrudRepository<BidirectionalCustomer, Long> {

}
