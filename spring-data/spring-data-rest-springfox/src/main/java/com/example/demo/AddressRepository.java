package com.example.demo;

import io.swagger.annotations.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@Api(tags = "Address Entity")
@RepositoryRestResource(path = "addresses")
public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {

  @ApiOperation("find all Addresses that are associated with a given Customer")
  List<Address> findByCustomerId(@Param("customerId") @ApiParam(name = "customerId", value = "ID of the customer", type = "body") Long customerId);

  @Override
  @SuppressWarnings("unchecked")
  @ApiOperation("saves a new Address")
  @ApiResponses({@ApiResponse(code = 201, message = "Created", response = Address.class)})
  Address save(Address address);

}
