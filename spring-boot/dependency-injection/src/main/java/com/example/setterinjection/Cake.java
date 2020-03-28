package com.example.setterinjection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.dependency.Flavor;
import com.example.dependency.Topping;

@Component
public class Cake {

	private Logger LOGGER = LoggerFactory.getLogger(Cake.class);
	
	private Flavor flavor;
	
	@Autowired
	private Topping toppings;
	
	public Cake() {
		LOGGER.info("Flavor from setter Injection : " + this.flavor);
	}
	
	@Autowired
	public void setFlavor(Flavor flavor) {
		LOGGER.info("Initialising flavor object using setter injection");
		this.flavor = flavor;
	}

	public Flavor getFlavor() {
		return flavor;
	}

	public Topping getToppings() {
		return toppings;
	}

	public void setToppings(Topping toppings) {
		this.toppings = toppings;
	}

	@Override
	public String toString() {
		return "Cake [LOGGER=" + LOGGER + ", flavor=" + flavor + ", toppings=" + toppings + "]";
	}



}
