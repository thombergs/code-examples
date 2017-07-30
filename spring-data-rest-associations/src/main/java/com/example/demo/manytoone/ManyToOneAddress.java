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
	private ManyToOneAddress customer;

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

	public ManyToOneAddress getCustomer() {
		return customer;
	}

	public void setCustomer(ManyToOneAddress customer) {
		this.customer = customer;
	}
}


