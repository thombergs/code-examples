package com.example.demo.manytoone;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ManyToOneCustomer {

	@Id
	@GeneratedValue
	private long id;

	@Column
	private String name;

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
