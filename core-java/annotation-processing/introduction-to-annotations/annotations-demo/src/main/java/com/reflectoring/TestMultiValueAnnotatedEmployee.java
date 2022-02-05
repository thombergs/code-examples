package com.reflectoring;

import java.lang.annotation.Annotation;

public class TestMultiValueAnnotatedEmployee {

    public static void main(String[] args) {

        MultiValueAnnotatedEmployee employee = new MultiValueAnnotatedEmployee();

        Annotation companyAnnotation = employee.getClass().getAnnotation(Company.class);
        Company company = (Company)companyAnnotation;

        System.out.println("Company Name: " + company.name());
        System.out.println("Company City: " + company.city());
    }
}
