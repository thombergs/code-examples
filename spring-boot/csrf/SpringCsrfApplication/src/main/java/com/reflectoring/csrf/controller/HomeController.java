package com.reflectoring.csrf.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping
    public String homePage(HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from("testCookie", "cookieVal")
                .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return "homePage";
    }
}
