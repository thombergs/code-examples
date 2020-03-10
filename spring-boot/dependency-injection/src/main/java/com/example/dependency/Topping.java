package com.example.dependency;

import org.springframework.stereotype.Component;

@Component
public class Topping {

	String toppingName;

	public String getToppingName() {
		return toppingName;
	}

	void setToppingName(String toppingName) {
		this.toppingName = toppingName;
	}

	@Override
	public String toString() {
		return "Topping [toppingName=" + toppingName + "]";
	}

}
