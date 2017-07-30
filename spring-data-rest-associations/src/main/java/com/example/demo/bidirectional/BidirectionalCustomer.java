package com.example.demo.bidirectional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BidirectionalCustomer {

	@Id
	@GeneratedValue
	private long id;

	@Column
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	private List<BidirectionalAddress> addresses = new ArrayList<>();

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public List<BidirectionalAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<BidirectionalAddress> addresses) {
		this.addresses = addresses;
	}

	@PrePersist
	@PreUpdate
	public void updateAddressAssociation(){
		for(BidirectionalAddress address : this.addresses){
			address.setCustomer(this);
		}
	}
}
