package com.example.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;
import java.util.List;

@Api(tags = "Address Entity")
@RepositoryRestResource(path = "addresses")
public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {

	@ApiOperation("find all Addresses that are associated with a given Customer")
	List<Address> findByCustomerId(@Param("customerId") @RequestParam @ApiParam(name="customerId", value="ID of the customer") Long customerId);

	@Override
	@SuppressWarnings("unchecked")
	@ApiOperation("saves a new Address")
	Address save(Address address);

}
