package com.example.test.setterinjection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.ExampleApplicationSI;
import com.example.setterinjection.Cake;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExampleApplicationSI.class)
public class TestCakeClassSetterInjection {

	@Autowired
	Cake cake;

	@Test
	public void testSetterInjection() {
		String testColor = cake.getFlavor().getColor();
		Assert.assertEquals(testColor, " White ");
		String toppingsName=cake.getToppings().getToppingName();
		
		//check if the dependency is not null
		if(toppingsName!=null) {
			Assert.assertEquals(toppingsName, "gems");
		}
		
	}
}
