package com.example.demo;

import javax.persistence.*;

@Entity
public class Address {

  @GeneratedValue
  @Id
  private Long id;

  @Column
  private String street;

  @ManyToOne
  private Customer customer;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
}


