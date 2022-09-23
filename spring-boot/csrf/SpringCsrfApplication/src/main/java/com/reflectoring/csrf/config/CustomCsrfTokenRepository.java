package com.reflectoring.csrf.config;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CustomCsrfTokenRepository implements CsrfTokenRepository {

    private String headerName = "X-CSRF-TOKEN";

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return new DefaultCsrfToken(headerName,"_csrf", generateRandomToken());
    }

    /**
     * Token implementation.
     * @return
     */
    private String generateRandomToken() {
        int random = ThreadLocalRandom.current().nextInt();
        return random + System.currentTimeMillis() + "";
    }

    /**
     * This will be your implementation.
     * @param token Csrf  token
     * @param request Sample Request
     * @param response Sample response
     */
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        String tokenValue = token != null ? token.getToken() : "";
        Cookie cookie = new Cookie("XSRF-TOKEN", tokenValue);
        cookie.setMaxAge(token != null ? -1 : 0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * This will be your implementation.
     * @param request Sample request
     * @return
     */
    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
        if (cookie == null) {
            return null;
        } else {
            String token = cookie.getValue();
            return !StringUtils.hasLength(token) ? null : new DefaultCsrfToken(headerName,"_csrf", token);
        }
    }
}
