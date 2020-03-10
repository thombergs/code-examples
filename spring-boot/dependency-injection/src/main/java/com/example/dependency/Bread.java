package com.example.dependency;

import org.springframework.stereotype.Component;

@Component
public class Bread {

	String breadType;

	public String getBreadType() {
		return breadType;
	}

	void setBreadType(String breadType) {
		this.breadType = breadType;
	}
}
