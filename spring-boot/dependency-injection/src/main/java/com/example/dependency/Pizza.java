package com.example.dependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Pizza {

	@Autowired
	private Topping toppings;

	Pizza(Topping toppings) {
		this.toppings = toppings;
	}

	public Topping getToppings() {
		return toppings;
	}

	public void setToppings(Topping toppings) {
		this.toppings = toppings;
	}

}
