package com.example.constructorinjection;

import com.example.ExampleApplicationCI;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExampleApplicationCI.class)
public class TestCakeClassConstructorInjection {

	@Autowired
	private Cake cake;

	@Test
	public void testConstructorInjection() {
		System.out.println(cake.toString());
		Assert.assertNotNull(cake.getFlavor());
	}
}
