package com.example.demo.bidirectional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "addresses_bi")
public interface BidirectionalAddressRepository extends CrudRepository<BidirectionalAddress, Long> {
}
