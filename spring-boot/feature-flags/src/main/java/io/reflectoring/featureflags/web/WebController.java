package io.reflectoring.featureflags.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
class WebController {

    private final UserSession userSession;

    WebController(UserSession userSession) {
        this.userSession = userSession;
    }

    @GetMapping(path = {"/"})
    ModelAndView chooseUser() {
        return new ModelAndView("/choose-user.html");
    }

    @GetMapping(path = {"/alice"})
    ModelAndView alice() {
        userSession.login("alice");
        return new ModelAndView("/features.html");
    }

    @GetMapping(path = {"/bob"})
    ModelAndView bob() {
        userSession.login("bob");
        return new ModelAndView("/features.html");
    }

    @GetMapping(path = {"/charlie"})
    ModelAndView charlie() {
        userSession.login("charlie");
        return new ModelAndView("/features.html");
    }

    @PostMapping(path = {"/click"})
    ModelAndView click() {
        userSession.recordClick();
        return new ModelAndView("/features.html");
    }

}
