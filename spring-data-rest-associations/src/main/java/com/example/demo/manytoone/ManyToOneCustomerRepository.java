package com.example.demo.manytoone;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "customers_mto")
public interface ManyToOneCustomerRepository extends CrudRepository<ManyToOneCustomer, Long> {

}
