package com.example.constructorinjection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.dependency.Flavor;


@Component
public class Cake {

	private static Logger LOGGER = LoggerFactory.getLogger(Cake.class);


	private Flavor flavor;

	public Cake(Flavor flavor) throws IllegalAccessException {
		
		//check if the required dependency is not null
		if (flavor != null) {
			this.flavor = flavor;
			LOGGER.info("Flavor from Constructor Injection : " + flavor);
		} else {
			throw new IllegalArgumentException("Cake cannot be created with null flavor object");
		}
	}

	public Flavor getFlavor() {
		return flavor;
	}
	
	@Override
	public String toString() {
		return "Cake [flavor=" + flavor + "]";
	}

}
