package io.reflectoring.validation;

import io.reflectoring.validation.service.OnCreate;
import io.reflectoring.validation.service.OnUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
