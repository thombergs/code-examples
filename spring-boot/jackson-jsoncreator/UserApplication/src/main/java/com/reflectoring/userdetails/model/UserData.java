package com.reflectoring.userdetails.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserData {

    private final Long id;

    private final String firstName;

    private final String lastName;

    private final String city;

    private LocalDate createdDate;

    //@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserData(@JsonProperty("id") long id, @JsonProperty("firstName") String firstName,
                    @JsonProperty("lastName") String lastName, @JsonProperty("city") String city) {
        System.out.println("Constructor properties arg");
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.createdDate = LocalDate.now();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public UserData(Map<String,String> map) throws JsonProcessingException {
        System.out.println("Constructor delegating arg: " + map.get("id"));
        this.id = Long.parseLong(map.get("id"));
        this.firstName = map.get("firstName");
        this.lastName = map.get("lastName");
        this.city = map.get("city");
    }

    public UserData(long id, String firstName,
                    String lastName, String city, LocalDate createdDate) {
        System.out.println("Constructor2");
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.createdDate = createdDate;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
