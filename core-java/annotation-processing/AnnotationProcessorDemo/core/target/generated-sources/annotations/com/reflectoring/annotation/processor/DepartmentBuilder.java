package com.reflectoring.annotation.processor;

import java.lang.String;

public final class DepartmentBuilder {
  private int id;

  private String name;

  public DepartmentBuilder id(int id) {
    this.id = id;
    return this;
  }

  public DepartmentBuilder name(String name) {
    this.name = name;
    return this;
  }

  public Department build() {
    Department department = new Department();
    department.setId(this.id);
    department.setName(this.name);
    return department;
  }
}
