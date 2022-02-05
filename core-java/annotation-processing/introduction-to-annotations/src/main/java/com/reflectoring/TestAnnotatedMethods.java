package com.reflectoring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class TestAnnotatedMethods {

    public static void main(String[] args) throws Exception {

        Class<AnnotatedMethods> annotatedMethodsClass = AnnotatedMethods.class;

        for (Method method : annotatedMethodsClass.getDeclaredMethods()) {

            Annotation annotation = method.getAnnotation(Test.class);
            Test test = (Test) annotation;

            // If the annotation is not null
            if (test != null) {

                try {
                    method.invoke(annotatedMethodsClass.getDeclaredConstructor().newInstance());
                } catch (Throwable ex) {
                    System.out.println(ex.getCause());
                }

            }
        }
    }
}


