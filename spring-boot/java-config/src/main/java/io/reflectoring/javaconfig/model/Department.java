package io.reflectoring.javaconfig.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@Entity
@Table(name = "departments")
public class Department extends BaseModel implements InitializingBean, DisposableBean {

    @NotBlank(message = "Department name should be provided.")
    private String name;

    @OneToMany(mappedBy = "department")
    @JsonManagedReference
    private Set<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "ORGANIZATION_ID")
    @JsonBackReference
    private Organization organization;

    public void addEmployee(final Employee employee) {
        if (employee != null) {
            employee.setDepartment(this);
            if (employees == null) {
                employees = new HashSet<>();
            }
            employees.add(employee);
        }
    }

    public void removeEmployee(final Employee employee) {
        if (employee != null) {
            employee.setDepartment(null);
            if (employees == null) {
                employees = new HashSet<>();
            }
            employees.remove(employee);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (employees == null) {
            employees = new HashSet<>();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (employees != null) {
            employees.clear();
        }
    }
}
