package com.test;

import com.reflectoring.annotation.processor.Employee;

public class BuilderTest {

    public static void main(String[] args) {

        Employee employee = new EmployeeBuilder()
                            .department("Sales")
                            .build();

        System.out.println("Employee dept: " + employee.getDepartment());
    }
}
