package com.reflectoring;

public class TestRepeatedAnnotation {

    public static void main(String[] args) {

        RepeatableCompany[] repeatableCompanies = RepeatedAnnotatedEmployee.class.getAnnotationsByType(RepeatableCompany.class);
        for (RepeatableCompany repeatableCompany : repeatableCompanies) {

            System.out.println("Name: " + repeatableCompany.name());
            System.out.println("City: " + repeatableCompany.city());
        }
    }
}
