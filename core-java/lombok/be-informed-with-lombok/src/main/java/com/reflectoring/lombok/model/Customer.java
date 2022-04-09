package com.reflectoring.lombok.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Customer {
    private String id;
    private String name;
    private Gender gender;
    private String dateOfBirth;
    private String age;
    private String socialSecurityNo;
}
