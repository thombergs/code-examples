/**
 * 
 */
package io.pratik.customerregistration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pratik.customerregistration.dtos.CustomerCreateRequest;
import io.pratik.customerregistration.dtos.CustomerDto;
import io.pratik.customerregistration.repositories.CustomerImageStore;
import io.pratik.customerregistration.repositories.CustomerProfileStore;
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
