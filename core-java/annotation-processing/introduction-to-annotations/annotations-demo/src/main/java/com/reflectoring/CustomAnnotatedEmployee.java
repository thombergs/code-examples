package com.reflectoring;

@Company
public class CustomAnnotatedEmployee {

    private int id;
    private String name;

    public CustomAnnotatedEmployee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void getEmployeeDetails(){

        System.out.println("Employee Id: " + id);
        System.out.println("Employee Name: " + name);
    }
}
