package com.example.test.constructorinjection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.ExampleApplicationCI;
import com.example.constructorinjection.Cake;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExampleApplicationCI.class)
public class TestCakeClassConstructorInjection {

//	@Mock
//	Flavor flavor;

	@InjectMocks
	Cake cake;

	@Test
	public void testConstructorInjection() {
		System.out.println(cake.toString());
		Assert.assertNotNull(cake.getFlavor());
	}
}
