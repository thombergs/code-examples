package com.example.demo.onetomany;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OneToManyCustomer {

	@Id
	@GeneratedValue
	private long id;

	@Column
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private List<OneToManyAddress> addresses = new ArrayList<>();

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<OneToManyAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<OneToManyAddress> addresses) {
		this.addresses = addresses;
	}

}
