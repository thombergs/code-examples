package io.reflectoring.cookie.controllers;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.reflectoring.cookie.utils.CookieUtil;

@RestController
@RequestMapping("/")
public class ServletCookieController {
	

	    @GetMapping("/create-servlet-cookie")
	    public String setCookie(HttpServletRequest request, HttpServletResponse response) {
          
	    	Cookie servletCookie = CookieUtil.createCookie("user-id", "c2FtLnNtaXRoQGV4YW1wbGUuY29t", 1 * 24 * 60 * 60, true, true, "/", request.getServerName());
	        response.addCookie(servletCookie);

	        return String.format("Cookie with name %s and value %s was created", servletCookie.getName(), servletCookie.getValue());
	    }

	    @GetMapping("/delete-servlet-cookie")
	    public String deleteCookie(HttpServletRequest request, HttpServletResponse response) {

	    	Cookie deleteServletookie = CookieUtil.createCookie("user-id", null, 0, true, true, "/", request.getServerName());
	        response.addCookie(deleteServletookie);

	        return String.format("Cookie with name %s was deleted", deleteServletookie.getName());
	    }

	    @GetMapping("/all-servlet-cookies")
	    public String readAllCookies(HttpServletRequest request) {
	    	
	    	return CookieUtil.readCookie(request, "user-id").orElse("cookie with name \"user-id\" is missing");
	    	
	    }

}
