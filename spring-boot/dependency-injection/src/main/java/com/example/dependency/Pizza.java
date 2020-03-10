package com.example.dependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Pizza {

	@Autowired
	Topping toppings;

	Pizza(Topping toppings) {
		this.toppings = toppings;
	}

	@Autowired
	public Topping getToppings() {
		System.out.println("Using field injection - " + this.toppings);
		return toppings;
	}

	public void setToppings(Topping toppings) {
		this.toppings = toppings;
	}

}
