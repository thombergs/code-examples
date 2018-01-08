package com.example.demo.onetomany;

import javax.persistence.*;

@Entity
public class OneToManyAddress {

	@GeneratedValue
	@Id
	private Long id;

	@Column
	private String street;

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

}


