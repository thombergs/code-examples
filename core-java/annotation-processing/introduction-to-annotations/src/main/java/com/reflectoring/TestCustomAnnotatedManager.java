package com.reflectoring;

import java.lang.annotation.Annotation;

public class TestCustomAnnotatedManager {

    public static void main(String[] args) {

        CustomAnnotatedManager manager = new CustomAnnotatedManager(1, "John Doe");
        manager.getEmployeeDetails();

        Annotation companyAnnotation = manager.getClass().getAnnotation(Company.class);
        Company company = (Company)companyAnnotation;

        System.out.println("Company Name: " + company.name());
        System.out.println("Company City: " + company.city());
    }
}
