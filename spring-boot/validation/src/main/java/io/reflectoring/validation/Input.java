package io.reflectoring.validation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@Entity
public class Input {

  @Id
  @GeneratedValue
  private Long id;

  @Min(value = 1, message = "{number.invalid}")
  @Max(value = 10, message = "{number.invalid}")
  private int numberBetweenOneAndTen;

  // Note that this is actually not a valid IP address pattern, since
  // it allows values greater than 255 per octet.
  @Pattern(regexp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$", message = "{ip-address.invalid}")
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
}
