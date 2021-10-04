package com.reflectoring.gymbuddy.dto.person;


public class PersonAddRequest {

  private String name;
  private String lastname;
  private String email;
  private String password;

  public PersonAddRequest(String name, String lastname, String email, String password){
    this.name = name;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
  }

  private PersonAddRequest(PersonAddRequestBuilder builder){
    this.name = builder.name;
    this.lastname = builder.lastname;
    this.email = builder.email;
    this.password = builder.password;
  }

  public String getName() {
    return name;
  }

  public String getLastname() {
    return lastname;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public static class PersonAddRequestBuilder{
    private String name;
    private String lastname;
    private String email;
    private String password;

    public PersonAddRequestBuilder(){}

    public PersonAddRequestBuilder name(String name){
      this.name = name;
      return this;
    }

    public PersonAddRequestBuilder lastname(String lastname){
      this.lastname = lastname;
      return this;
    }

    public PersonAddRequestBuilder email(String email){
      this.email = email;
      return this;
    }

    public PersonAddRequestBuilder password(String password){
      this.password = password;
      return this;
    }

    public PersonAddRequest build(){
      return new PersonAddRequest(this);
    }
  }
}
