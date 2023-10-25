package io.reflectoring.javaconfig.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@Entity
@Table(name = "employees")
public class Employee extends BaseModel {
    private String title;

    private String firstName;

    private String lastName;

    private String designation;

    @Setter(AccessLevel.PRIVATE)
    private UUID secretCode;

    @NotBlank(message = "Mobile number must be provided.")
    @Pattern(regexp = "(^$|[0-9]{10})")
    private String mobileNumber;

    @PastOrPresent(message = "Employee cannot join on future date.")
    private Date joiningDate;

    @NotBlank(message = "Email should be provided.")
    @Email(message = "Email should be valid.")
    private String email;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    @JsonBackReference
    private Department department;

    public void init() {
        setSecretCode(UUID.randomUUID());
    }

    public void destroy() {
        setSecretCode(null);
    }

}
