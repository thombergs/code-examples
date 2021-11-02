package com.example.demo;

public class CustomerAndAddress {

  private Customer customer;

  private Address address;

  public CustomerAndAddress(Customer customer, Address address) {
    this.customer = customer;
    this.address = address;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}
