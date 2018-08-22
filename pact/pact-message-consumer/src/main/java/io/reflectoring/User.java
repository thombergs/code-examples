package io.reflectoring;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class User {

	@NotNull
	private long id;

	@NotNull
	private String name;

}
