/**
 * 
 */
package io.pratik.dynamodbspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pratik.dynamodbspring.models.Customer;
import io.pratik.dynamodbspring.repositories.CustomerRepository;

/**
 * @author pratikdas
 *
 */
@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public void createCustomer(final Customer customer) {
		customerRepository.save(customer);
	}

}
