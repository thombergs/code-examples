package io.reflectoring.testing.web;

import io.reflectoring.testing.domain.RegisterUseCase;
import io.reflectoring.testing.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
class RegisterMvcController {

  private final RegisterUseCase registerUseCase;

  @PostMapping("/mvc/register/")
  ModelAndView register(@ModelAttribute("user") UserResource userResource){

    User user = new User(
            userResource.getName(),
            userResource.getEmail());

    registerUseCase.registerUser(user, false);

    ModelAndView modelAndView = new ModelAndView("registration_success.html");
    modelAndView.addObject("username", user.getName());

    return modelAndView;
  }

}
