package com.reflectoring.userdetails.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.reflectoring.userdetails.persistence.Views;

import java.time.LocalDate;
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserData {

    @JsonView(value = {Views.GetView.class, Views.UserSummary.class, Views.ExternalView.class})
    private long id;

    @JsonView(value = {Views.GetView.class, Views.UserSummary.class, Views.ExternalView.class})
    private String firstName;

    @JsonView(value = {Views.GetView.class, Views.UserSummary.class, Views.ExternalView.class})
    private String lastName;

    @JsonView(value = {Views.GetView.class, Views.UserSummary.class, Views.ExternalView.class})
    private String dob;

    @JsonView(value = {Views.GetView.class, Views.PatchView.class, Views.UserSummary.class, Views.ExternalView.class})
    private String address;

    @JsonView(value = {Views.GetView.class, Views.PatchView.class, Views.UserSummary.class, Views.ExternalView.class})
    private String suburb;

    @JsonView(value = {Views.GetView.class, Views.PatchView.class, Views.UserSummary.class, Views.ExternalView.class})
    private String city;

    private boolean internalUser;

    private String additionalData;

    @JsonView(value = {Views.GetView.class, Views.InternalView.class})
    private String loginId;

    @JsonView(value = {Views.GetView.class, Views.InternalView.class})
    private String loginPassword;
    @JsonView(value = {Views.GetView.class, Views.InternalView.class})
    private String ssnNumber;

    @JsonView(Views.UserDetailedSummary.class)
    private String createdBy;

    @JsonView(Views.UserDetailedSummary.class)
    private LocalDate createdDate;

    @JsonView(Views.UserDetailedSummary.class)
    private String updatedBy;

    @JsonView(Views.UserDetailedSummary.class)
    private LocalDate updatedDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @JsonIgnore
    public boolean isInternalUser() {
        return internalUser;
    }

    public void setInternalUser(boolean internalUser) {
        internalUser = internalUser;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getSsnNumber() {
        return ssnNumber;
    }

    public void setSsnNumber(String ssnNumber) {
        this.ssnNumber = ssnNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob='" + dob + '\'' +
                ", address='" + address + '\'' +
                ", suburb='" + suburb + '\'' +
                ", city='" + city + '\'' +
                ", internalUser=" + internalUser +
                ", loginId='" + loginId + '\'' +
                ", loginPassword='" + loginPassword + '\'' +
                ", ssnNumber='" + ssnNumber + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
