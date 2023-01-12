package com.reflectoring.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String homePage(HttpServletResponse response) {
        return "homePage";
    }

    @GetMapping("/invalidSession")
    public String invalidSession(HttpServletResponse response) {
        return "invalidSession";
    }
}
