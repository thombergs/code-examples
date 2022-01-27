package com.reflectoring.annotation.processor;

import java.lang.String;

public final class EmployeeBuilder {
  private int id;

  private String department;

  public EmployeeBuilder id(int id) {
    this.id = id;
    return this;
  }

  public EmployeeBuilder department(String department) {
    this.department = department;
    return this;
  }

  public Employee build() {
    Employee employee = new Employee();
    employee.setId(this.id);
    employee.setDepartment(this.department);
    return employee;
  }
}
