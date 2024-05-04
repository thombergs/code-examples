package io.reflectoring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserCreationRequestDto {

	@NotBlank(message = "Name must not be empty")
	private String name;

	@NotBlank(message = "EmailId must not be empty")
	@Email(message = "EmailId must be of valid format")
	private String emailId;

	@NotBlank(message = "Password must not be empty")
	private String password;

}