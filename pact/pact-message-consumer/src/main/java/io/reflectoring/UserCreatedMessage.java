package io.reflectoring;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserCreatedMessage {

	@NotNull
	private String messageUuid;

	@NotNull
	private User user;

}
