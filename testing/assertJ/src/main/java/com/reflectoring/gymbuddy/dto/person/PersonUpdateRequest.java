package com.reflectoring.gymbuddy.dto.person;


public class PersonUpdateRequest {

  private String name;
  private String lastname;
  private String email;
  private String password;

  public PersonUpdateRequest(String name, String lastname, String email, String password){
    this.name = name;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
  }

  private PersonUpdateRequest(PersonUpdateRequestBuilder builder){
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

  public class PersonUpdateRequestBuilder{
    private String name;
    private String lastname;
    private String email;
    private String password;

    public PersonUpdateRequestBuilder(){}

    public PersonUpdateRequestBuilder name(String name){
      this.name = name;
      return this;
    }

    public PersonUpdateRequestBuilder lastname(String lastname){
      this.lastname = lastname;
      return this;
    }

    public PersonUpdateRequestBuilder email(String email){
      this.email = email;
      return this;
    }

    public PersonUpdateRequestBuilder password(String password){
      this.password = password;
      return this;
    }

    public PersonUpdateRequest build(){
      return new PersonUpdateRequest(this);
    }
  }
}
