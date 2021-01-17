package io.reflectoring.cookie.controllers;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

@RestController
public class SpringCookieController {
	
	 @GetMapping("/create-spring-cookie")
	    public ResponseEntity setCookie() {
		 
		 ResponseCookie resCookie = ResponseCookie.from("cookie-name", "spring-cookie")
		            .httpOnly(true)
		            .secure(true)
		            .path("/")
		            .maxAge(1 * 24 * 60 * 60)
		            .domain("localhost")
		            .build();

	        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString()).build();

	    }

	    @GetMapping("/delete-spring-cookie")
	    public ResponseEntity deleteCookie() {

	        // create a cookie
	    	 ResponseCookie resCookie = ResponseCookie.from("cookie-name", null)
			            .build();

		        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString()).build();

	    }
	    
	    @GetMapping("/read-spring-cookie")
	    public String readCookie(@CookieValue(name = "cookie-name", defaultValue = "default-spring-cookie") String cookieName) {
	        return String.format("value of the cookie with name cookie-name is: %s", cookieName);
	    }

}
