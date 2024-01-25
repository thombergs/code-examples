package com.reflectoring.userdetails.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserTypeData {

    private final UserData userData;

    private final UserType userType;


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserTypeData(@JsonProperty("userData") UserData userData, @JsonProperty("userType") UserType userType) {
        this.userData = userData;
        this.userType = userType;
    }
}
