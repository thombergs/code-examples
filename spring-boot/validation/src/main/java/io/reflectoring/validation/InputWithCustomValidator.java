package io.reflectoring.validation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import io.reflectoring.validation.service.OnCreate;
import io.reflectoring.validation.service.OnUpdate;

@Entity
public class InputWithCustomValidator {

  @Id
  @GeneratedValue
  @NotNull(groups = OnUpdate.class)
  @Null(groups = OnCreate.class)
  private Long id;

  @Min(1)
  @Max(10)
  @Column
  private int numberBetweenOneAndTen;

  @IpAddress
  @Column
  private String ipAddress;

  public int getNumberBetweenOneAndTen() {
    return numberBetweenOneAndTen;
  }

  public void setNumberBetweenOneAndTen(int numberBetweenOneAndTen) {
    this.numberBetweenOneAndTen = numberBetweenOneAndTen;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
