package com.example.demo.onetomany;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "customers_otm")
public interface OneToManyCustomerRepository extends CrudRepository<OneToManyCustomer, Long> {

}
