package com.example.test.fieldinjection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.ExampleApplicationSI;
import com.example.fieldinjection.IceCream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExampleApplicationSI.class)
public class TestCookieClassFieldInjection {

	@InjectMocks
	IceCream iceCream;

	@Test
	public void testSetterInjection() {
		System.out.println(iceCream.toString());
		Assert.assertNotNull(iceCream.getToppings());
	}
}
