package com.reflectoring.userdetails.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.reflectoring.userdetails.persistence.User;

import java.util.NoSuchElementException;

public enum UserType {
    EXTERNAL("100"),
    INTERNAL("101");

    private String id;

    UserType(String id) {
        this.id = id;
    }

    @JsonValue
    public String getId() {
        return this.id;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserType fromId(String id) {
        System.out.println("Entered creator method: " + id);
        for (UserType type : UserType.values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }

        throw new NoSuchElementException(String.format(
                "%s with id = %d does not exist",
                UserType.class.getName(),
                id));
    }
}
