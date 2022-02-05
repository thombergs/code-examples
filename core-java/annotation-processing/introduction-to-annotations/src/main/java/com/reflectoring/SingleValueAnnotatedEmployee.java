package com.reflectoring;

@SingleValueAnnotationCompany("XYZ")
public class SingleValueAnnotatedEmployee {

    private int id;
    private String name;

    public SingleValueAnnotatedEmployee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void getEmployeeDetails(){

        System.out.println("Employee Id: " + id);
        System.out.println("Employee Name: " + name);
    }
}
