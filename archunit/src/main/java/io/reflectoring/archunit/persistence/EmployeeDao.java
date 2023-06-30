package io.reflectoring.archunit.persistence;

public class EmployeeDao {

    public Employee findEmployee() {
        return new Employee(1, "name", true);
    }
}
