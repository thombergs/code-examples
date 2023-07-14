package io.reflectoring.archunit.service;

import io.reflectoring.archunit.persistence.Employee;
import io.reflectoring.archunit.persistence.EmployeeDao;

public class EmployeeService {
    public EmployeeResponse getEmployee() {
        EmployeeDao employeeDao = new EmployeeDao();
        Employee employee = employeeDao.findEmployee();
        return new EmployeeResponse(
            employee.id(),
            employee.name(),
            employee.active()
        );
    }
}
