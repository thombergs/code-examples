package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements ApplicationRunner {

	private CustomerRepository mtoCustomerRepository;

	private AddressRepository mtoAddressRepository;

	@Autowired
	public Initializer(CustomerRepository mtoCustomerRepository, AddressRepository mtoAddressRepository) {
		this.mtoCustomerRepository = mtoCustomerRepository;
		this.mtoAddressRepository = mtoAddressRepository;
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		Customer mtoCustomer = new Customer();
		mtoCustomer.setName("Tom");
		mtoCustomerRepository.save(mtoCustomer);

		Address mtoAddress = new Address();
		mtoAddress.setStreet("Elm Street");
		mtoAddressRepository.save(mtoAddress);

		mtoAddress = new Address();
		mtoAddress.setStreet("High Street");
		mtoAddressRepository.save(mtoAddress);
	}
}
