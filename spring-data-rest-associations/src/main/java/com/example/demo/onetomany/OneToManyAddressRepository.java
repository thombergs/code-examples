package com.example.demo.onetomany;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "addresses_otm")
public interface OneToManyAddressRepository extends CrudRepository<OneToManyAddress, Long> {
}
