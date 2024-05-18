package io.reflectoring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreatedEventDto {

	private String name;
	private String emailId;

}