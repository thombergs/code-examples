package com.example.fieldinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.dependency.Topping;

@Component
public class IceCream {

	@Autowired
	Topping toppings;

	public Topping getToppings() {
		return toppings;
	}

	void setToppings(Topping toppings) {
		this.toppings = toppings;
	}

	@Override
	public String toString() {
		return "IceCream [toppings=" + toppings + "]";
	}

}
