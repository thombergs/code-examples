package com.example.constructorinjection;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.example.dependency.Flavor;

@Component
public class Cake {

	private Flavor flavor;

	Cake(Flavor flavor) {

		// check if the required dependency is not null
		if (Objects.requireNonNull(flavor) != null) {
			this.flavor = flavor;
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
