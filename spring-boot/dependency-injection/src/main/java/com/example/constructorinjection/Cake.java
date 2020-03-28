package com.example.constructorinjection;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.example.dependency.Flavor;

@Component
public class Cake {

	private Flavor flavor;

	Cake(Flavor flavor) {
		Objects.requireNonNull(flavor);
		this.flavor = flavor;
	}

	public Flavor getFlavor() {
		return flavor;
	}

	@Override
	public String toString() {
		return "Cake [flavor=" + flavor + "]";
	}


}
