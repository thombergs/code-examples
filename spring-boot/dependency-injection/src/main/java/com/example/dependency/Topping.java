package com.example.dependency;

import org.springframework.stereotype.Component;

@Component
public class Topping {

	private String toppingName;

	public String getToppingName() {
		return toppingName;
	}

	public void setToppingName(String toppingName) {
		this.toppingName = toppingName;
	}

	@Override
	public String toString() {
		return "Topping [toppingName=" + toppingName + "]";
	}


}
