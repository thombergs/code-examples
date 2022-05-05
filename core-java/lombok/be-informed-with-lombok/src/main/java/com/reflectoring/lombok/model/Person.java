package com.reflectoring.lombok.model;

import lombok.Value;

import java.util.List;

@Value
public class Person {
    private String firstName;
    private String lastName;
    private String socialSecurityNo;
    private List<String> hobbies;
}
