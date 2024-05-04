package io.reflectoring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.reflectoring.dto.UserCreationRequestDto;
import io.reflectoring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody final UserCreationRequestDto userCreationRequest) {
		userService.create(userCreationRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
