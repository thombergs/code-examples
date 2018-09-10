package io.reflectoring;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class UserCreatedMessage {

	@NotNull
	private String messageUuid;

	@NotNull
	private User user;

}
