package com.reflectoring.csrf.config;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ThreadLocalRandom;

public class MyCsrfRepository implements CsrfTokenRepository {


    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return new DefaultCsrfToken("mehmetozan", "guven", generateRandomToken());
    }

    /**
     * Dummy implementation.
     * @return
     */
    private String generateRandomToken() {
        int random = ThreadLocalRandom.current().nextInt();
        return random + "abcdfe";
    }

    /**
     * This will be your implementation.
     * @param token Csrf  token
     * @param request Sample Request
     * @param response Sample response
     */
    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     * This will be your implementation.
     * @param request Sample request
     * @return
     */
    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return null;
    }
}
