package com.example.test.constructorinjection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.ExampleApplicationCI;
import com.example.constructorinjection.Cake;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExampleApplicationCI.class)
public class TestCakeClassConstructorInjection {

	@Autowired
	Cake cake;

	@Test
	public void testConstructorInjection() {
		String testColor = cake.getFlavor().getColor();
		Assert.assertEquals(testColor, " White ");
	}
}
