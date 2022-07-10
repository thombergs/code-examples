package com.reflectoring.pojo;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Car {
    @JsonSetter("carBrand")
    private String brand;
    private Map<String, String> unrecognizedFields = new HashMap<>();

    @JsonAnySetter
    public void allSetter(String fieldName, String fieldValue) {
        unrecognizedFields.put(fieldName, fieldValue);
    }
}
