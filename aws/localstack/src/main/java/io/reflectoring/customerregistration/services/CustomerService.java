/**
 * 
 */
package io.reflectoring.customerregistration.services;

import io.reflectoring.customerregistration.dtos.CustomerDto;
import io.reflectoring.customerregistration.repositories.CustomerImageStore;
import io.reflectoring.customerregistration.repositories.CustomerProfileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reflectoring.customerregistration.dtos.CustomerCreateRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Pratik Das
 *
 */
@Service
@Slf4j
public class CustomerService {
	
	
	private CustomerImageStore customerImageStore;
	private CustomerProfileStore customerProfileStore;
	
	
	@Autowired
	public CustomerService(CustomerImageStore customerImageStore, CustomerProfileStore customerProfileStore) {
		super();
		this.customerImageStore = customerImageStore;
		this.customerProfileStore = customerProfileStore;
	}

	public CustomerDto fetchCustomer(final String customerID) {
		return customerProfileStore.fetchProfile(customerID);
		
	}


	public String createCustomer(final CustomerCreateRequest request) {
		String customerKey = customerProfileStore.createProfile(request);
		customerImageStore.saveImage(request,customerKey);
		return customerKey;
	}

}
