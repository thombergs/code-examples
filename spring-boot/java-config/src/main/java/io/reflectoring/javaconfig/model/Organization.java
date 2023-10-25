package io.reflectoring.javaconfig.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@Entity
@Table(name = "organizations")
public class Organization extends BaseModel {

    @NotBlank(message = "Organization name should be provided.")
    private String name;

    @OneToMany(mappedBy = "organization",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    @JsonManagedReference
    private Set<Department> departments;

    public void addDepartment(final Department department) {
        if (department != null) {
            department.setOrganization(this);
            if (departments == null) {
                departments = new HashSet<>();
            }
            departments.add(department);
        }
    }

    public void removeDepartment(final Department department) {
        if (department != null) {
            department.setOrganization(null);
            if (departments == null) {
                departments = new HashSet<>();
            }
            departments.remove(department);
        }
    }
}
