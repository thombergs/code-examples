package io.reflectoring.validation;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ProgrammerStereotype
public class ProgrammerRegisterationRequest {

  @NotBlank
  private String programmingLanguage;

  @NotBlank
  private String favoriteIDE;

}