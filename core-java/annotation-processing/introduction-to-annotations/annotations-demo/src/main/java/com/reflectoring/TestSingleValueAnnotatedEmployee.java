package com.reflectoring;

import java.lang.annotation.Annotation;

public class TestSingleValueAnnotatedEmployee {

    public static void main(String[] args) {

        SingleValueAnnotatedEmployee employee = new SingleValueAnnotatedEmployee(1, "John Doe");
        employee.getEmployeeDetails();

        Annotation companyAnnotation = employee.getClass().getAnnotation(SingleMemberCompany.class);
        SingleMemberCompany company = (SingleMemberCompany)companyAnnotation;

        System.out.println("Company Name: " + company.value());
    }
}
