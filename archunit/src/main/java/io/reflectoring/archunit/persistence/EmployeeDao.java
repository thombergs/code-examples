package io.reflectoring.archunit.persistence;

import io.reflectoring.archunit.model.Employee;

public class EmployeeDao {

    public Employee findEmployee() {
        return new Employee(1, "name", true);
    }
}
