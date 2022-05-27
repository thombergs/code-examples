package io.reflectoring.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Address {
    private String houseNo;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
