package com.example.demo.manytoone;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "addresses_mto")
public interface ManyToOneAddressRepository extends CrudRepository<ManyToOneAddress, Long> {
}
