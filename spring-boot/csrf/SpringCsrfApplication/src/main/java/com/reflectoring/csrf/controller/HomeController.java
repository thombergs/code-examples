package com.reflectoring.csrf.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String homePage(HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from("testCookie", "cookieVal")
                .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return "homePage";
    }
}
