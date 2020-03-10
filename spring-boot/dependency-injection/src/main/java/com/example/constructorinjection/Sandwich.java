package com.example.constructorinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.dependency.Bread;
import com.example.dependency.Topping;

@Component
public class Sandwich {

	Topping toppings;
	Bread breadType;

	Sandwich(Topping toppings) {
		this.toppings = toppings;
	}

	@Autowired
	Sandwich(Topping toppings, Bread breadType) {
		this.toppings = toppings;
		this.breadType = breadType;
	}

	public Topping getToppings() {
		return toppings;
	}

	public Bread getBreadType() {
		return breadType;
	}
}
