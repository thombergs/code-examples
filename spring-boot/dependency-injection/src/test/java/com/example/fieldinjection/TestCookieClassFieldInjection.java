package com.example.fieldinjection;

import com.example.ExampleApplicationFI;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.ExampleApplicationSI;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExampleApplicationFI.class)
public class TestCookieClassFieldInjection {

	@Autowired
	private IceCream iceCream;

	@Test
	public void testFieldInjection() {
		System.out.println(iceCream.toString());
		Assert.assertNotNull(iceCream.getToppings());
	}
}
