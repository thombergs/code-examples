package com.example.setterinjection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.ExampleApplicationSI;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExampleApplicationSI.class)
public class TestCakeClassSetterInjection {

	@Autowired
	private Cake cake;

	@Test
	public void testSetterInjection() {
		Assert.assertNotNull(cake.getFlavor());
		Assert.assertNotNull(cake.getToppings());
	}
}
