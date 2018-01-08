package com.example.demo.bidirectional;

import javax.persistence.*;

@Entity
public class BidirectionalAddress {

	@GeneratedValue
	@Id
	private Long id;

	@Column
	private String street;

	@ManyToOne
	private BidirectionalCustomer customer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public BidirectionalCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(BidirectionalCustomer customer) {
		this.customer = customer;
	}
}


