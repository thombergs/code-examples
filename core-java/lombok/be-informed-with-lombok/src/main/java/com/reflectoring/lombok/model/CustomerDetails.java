package com.reflectoring.lombok.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
/*
    **
    * This is a poorly defined model class.
    * To make it more readable move address attributes and driver license related attributes to their separate classes
    * public class CustomerDetails {

            private String id;
            private String name;
            private String buildingNm;
            private String blockNo;
            private String streetNm;
            private String city;
            private int postcode;
            private String state;
            private String country;
            private Gender gender;
            private String dateOfBirth;
            private String email;
            private String phoneNo;
            private String drivingLicenseNo;
            private String licenseIssueState;
   }
 */
public class CustomerDetails {

    private String id;
    private String name;
    private Address address;
    private Gender gender;
    private String dateOfBirth;
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
