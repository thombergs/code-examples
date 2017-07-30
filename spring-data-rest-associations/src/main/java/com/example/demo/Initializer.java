package com.example.demo;

import com.example.demo.bidirectional.BidirectionalAddress;
import com.example.demo.bidirectional.BidirectionalAddressRepository;
import com.example.demo.bidirectional.BidirectionalCustomer;
import com.example.demo.bidirectional.BidirectionalCustomerRepository;
import com.example.demo.manytoone.ManyToOneAddress;
import com.example.demo.manytoone.ManyToOneAddressRepository;
import com.example.demo.manytoone.ManyToOneCustomer;
import com.example.demo.manytoone.ManyToOneCustomerRepository;
import com.example.demo.onetomany.OneToManyAddress;
import com.example.demo.onetomany.OneToManyAddressRepository;
import com.example.demo.onetomany.OneToManyCustomer;
import com.example.demo.onetomany.OneToManyCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements ApplicationRunner {

	private BidirectionalCustomerRepository biCustomerRepository;

	private OneToManyCustomerRepository otmCustomerRepository;

	private ManyToOneCustomerRepository mtoCustomerRepository;

	private BidirectionalAddressRepository biAddressRepository;

	private OneToManyAddressRepository otmAddressRepository;

	private ManyToOneAddressRepository mtoAddressRepository;

	@Autowired
	public Initializer(BidirectionalCustomerRepository biCustomerRepository, OneToManyCustomerRepository otmCustomerRepository, ManyToOneCustomerRepository mtoCustomerRepository, BidirectionalAddressRepository biAddressRepository, OneToManyAddressRepository otmAddressRepository, ManyToOneAddressRepository mtoAddressRepository) {
		this.biCustomerRepository = biCustomerRepository;
		this.otmCustomerRepository = otmCustomerRepository;
		this.mtoCustomerRepository = mtoCustomerRepository;
		this.biAddressRepository = biAddressRepository;
		this.otmAddressRepository = otmAddressRepository;
		this.mtoAddressRepository = mtoAddressRepository;
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		BidirectionalCustomer bidirectionalCustomer = new BidirectionalCustomer();
		bidirectionalCustomer.setName("Tom");
		biCustomerRepository.save(bidirectionalCustomer);

		BidirectionalAddress bidirectionalAddress = new BidirectionalAddress();
		bidirectionalAddress.setStreet("Elm Street");
		biAddressRepository.save(bidirectionalAddress);

		bidirectionalAddress = new BidirectionalAddress();
		bidirectionalAddress.setStreet("High Street");
		biAddressRepository.save(bidirectionalAddress);

		OneToManyCustomer otmCustomer = new OneToManyCustomer();
		otmCustomer.setName("Tom");
		otmCustomerRepository.save(otmCustomer);

		OneToManyAddress otmAddress = new OneToManyAddress();
		otmAddress.setStreet("Elm Street");
		otmAddressRepository.save(otmAddress);

		otmAddress = new OneToManyAddress();
		otmAddress.setStreet("High Street");
		otmAddressRepository.save(otmAddress);

		ManyToOneCustomer mtoCustomer = new ManyToOneCustomer();
		mtoCustomer.setName("Tom");
		mtoCustomerRepository.save(mtoCustomer);

		ManyToOneAddress mtoAddress = new ManyToOneAddress();
		mtoAddress.setStreet("Elm Street");
		mtoAddressRepository.save(mtoAddress);

		mtoAddress = new ManyToOneAddress();
		mtoAddress.setStreet("High Street");
		mtoAddressRepository.save(mtoAddress);
	}
}
