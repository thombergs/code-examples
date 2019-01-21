package io.reflectoring.testing.web;

import javax.validation.Valid;

import io.reflectoring.testing.domain.RegisterUseCase;
import io.reflectoring.testing.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterRestController {

  private final RegisterUseCase registerUseCase;

  @PostMapping("/forums/{forumId}/register")
  UserResource register(
          @PathVariable("forumId") Long forumId,
          @Valid @RequestBody UserResource userResource,
          @RequestParam("sendWelcomeMail") boolean sendWelcomeMail) {

    User user = new User(
            userResource.getName(),
            userResource.getEmail());
    Long userId = registerUseCase.registerUser(user, sendWelcomeMail);

    return new UserResource(
            user.getName(),
            user.getEmail());
  }

}
