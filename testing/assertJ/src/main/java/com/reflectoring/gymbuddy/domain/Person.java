package com.reflectoring.gymbuddy.domain;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "person")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @NotNull
  @NotBlank
  private String name;

  @NotNull
  @NotBlank
  private String lastname;

  @NotNull
  @NotBlank
  private String email;

  @NotNull
  @NotBlank
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "friends",
    joinColumns = @JoinColumn(name = "a", referencedColumnName = "id",table = "person"),
    inverseJoinColumns = @JoinColumn(name = "b", referencedColumnName = "id", table = "person"))
  private Set<Person> friends;

  @OneToMany(mappedBy = "person",fetch = FetchType.EAGER)
  private List<Session> sessions;

  public Person(){}

  public Person(long id, String name, String lastname, String email, String password, Set<Person> friends, List<Session> sessions){
    this.id = id;
    this.name = name;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
    this.friends = friends;
    this.sessions = sessions;
  }
  private Person(PersonBuilder builder){
    this.id = builder.id;
    this.name = builder.name;
    this.lastname = builder.lastname;
    this.email = builder.email;
    this.password = builder.password;
    this.friends = builder.friends;
    this.sessions = builder.sessions;
  }

  public long getId() {
    return id;
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

  public void setName(String name) {
    this.name = name;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Person> getFriends() {
    return friends;
  }

  public List<Session> getSessions() {
    return sessions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Person person = (Person) o;
    return id == person.id && email.equals(person.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email);
  }

  public static class PersonBuilder{
    private long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private Set<Person> friends;
    private List<Session> sessions;

    public PersonBuilder(){}

    public PersonBuilder id(long id){
      this.id = id;
      return this;
    }

    public PersonBuilder name(String name){
      this.name = name;
      return this;
    }

    public PersonBuilder lastname(String lastname){
      this.lastname = lastname;
      return this;
    }

    public PersonBuilder email(String email){
      this.email = email;
      return this;
    }
    public PersonBuilder password(String password){
      this.password = password;
      return this;
    }
    public PersonBuilder friends(Set<Person> friends){
      this.friends = friends;
      return this;
    }

    public PersonBuilder sessions(List<Session> sessions){
      this.sessions = sessions;
      return this;
    }

    public Person build(){
      return new Person(this);
    }
  }

}
