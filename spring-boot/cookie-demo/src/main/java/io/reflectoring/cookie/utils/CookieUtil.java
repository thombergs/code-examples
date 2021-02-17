package io.reflectoring.cookie.utils;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
	
	public static Cookie createCookie(String name, String value, int expiry, boolean isSecure, boolean httpOnly, String path, String domain ) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expiry);
        cookie.setSecure(true);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath(path); 
        cookie.setDomain(domain);
        return cookie;
	}
	
	public static Optional<String> readCookie(HttpServletRequest request, String name) {
        return Arrays.stream(request.getCookies())
          .filter(cookie -> name.equals(cookie.getName()))
          .map(Cookie::getValue)
          .findAny();
    }

}
