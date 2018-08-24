package io.reflectoring;

import java.lang.reflect.Method;
import java.util.Set;

import au.com.dius.pact.provider.PactVerifyProvider;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;

public class ReflectionsTest {

	@Test
	public void test() {
		Reflections r = new Reflections(ClasspathHelper.forPackage("io.reflectoring"), new MethodAnnotationsScanner());
		Set<Method> methods = r.getMethodsAnnotatedWith(PactVerifyProvider.class);
		System.out.println(methods);
	}
}
