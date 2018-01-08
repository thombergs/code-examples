package com.example.demo.manytoone;

import javax.persistence.*;

@Entity
public class ManyToOneAddress {

	@GeneratedValue
	@Id
	private Long id;

	@Column
	private String street;

	@ManyToOne
	private ManyToOneCustomer customer;

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

	public ManyToOneCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(ManyToOneCustomer customer) {
		this.customer = customer;
	}
}


