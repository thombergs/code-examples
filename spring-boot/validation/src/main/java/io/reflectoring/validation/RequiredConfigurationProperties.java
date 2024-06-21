package io.reflectoring.validation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.jwt")
public class RequiredConfigurationProperties {

  @NotBlank
  private String privateKey;

  @NotNull
  @Positive
  private Integer validityMinutes;

}