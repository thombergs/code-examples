package com.reflectoring.lombok.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CustomerDetails {

    private String id;
    private String name;
    private Address address;
    private Gender gender;
    private String dateOfBirth;
    private String age;
    private String socialSecurityNo;
    private Contact contactDetails;
    private DriverLicense driverLicense;

    @Data
    @Builder
    @AllArgsConstructor
    public static class Address {
        private String id;
        private String buildingNm;
        private String blockNo;
        private String streetNm;
        private String city;
        private int postcode;
        private String state;
        private String country;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class DriverLicense {
        private String drivingLicenseNo;
        private String licenseIssueState;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Contact {
        private String id;
        private String email;
        private String phoneNo;

    }
}
