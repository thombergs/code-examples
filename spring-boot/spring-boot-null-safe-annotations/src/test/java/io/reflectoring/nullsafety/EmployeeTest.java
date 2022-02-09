package io.reflectoring.nullsafety;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void employeeShouldHaveValidDetails() {
        final Employee employee = new Employee("ID001", "Jane Dia", LocalDate.of(2019, 12, 31));
        employee.setPastEmployment(null);
        assertAll(() -> {
            assertNotNull(employee.getId());
            assertNotNull(employee.getName());
            assertNotNull(employee.getJoiningDate());
            assertNull(employee.getPastEmployment());
        });
    }

}
