package io.reflectoring.nullsafety;

import java.time.LocalDate;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

class Employee {

    @NonNull
    String id;
    String name;
    LocalDate joiningDate;
    @Nullable
    String pastEmployment;

    // constructor

    public Employee(String id, String name, LocalDate joiningDate) {
        this.id = id;
        this.name = name;
        this.joiningDate = joiningDate;
    }

    // standard getters-setters

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    LocalDate getJoiningDate() {
        return joiningDate;
    }

    @Nullable
    String getPastEmployment() {
        return pastEmployment;
    }

    void setPastEmployment(@Nullable String pastEmployment) {
        this.pastEmployment = pastEmployment;
    }
}
