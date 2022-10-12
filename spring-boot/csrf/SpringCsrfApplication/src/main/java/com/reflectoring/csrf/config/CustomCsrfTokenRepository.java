package com.reflectoring.csrf.config;

import com.reflectoring.csrf.model.Token;
import com.reflectoring.csrf.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CustomCsrfTokenRepository implements CsrfTokenRepository {

    private static final Logger log = LoggerFactory.getLogger(CustomCsrfTokenRepository.class);

    @Autowired
    public TokenRepository tokenRepository;

    private String headerName = "X-CSRF-TOKEN";

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        log.info("Inside custom generateToken()");
        return new DefaultCsrfToken(headerName,"_csrf", generateRandomToken());
    }

    /**
     * Token implementation.
     * @return
     */
    private String generateRandomToken() {
        log.info("Inside custom generateRandomToken()");
        int random = ThreadLocalRandom.current().nextInt();
        return random + System.currentTimeMillis() + "";
    }

    /**
     * Save/Update tokens in DB
     * @param token Csrf  token
     * @param request Sample Request
     * @param response Sample response
     */
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        log.info("Inside custom saveToken()");
        String username = request.getParameter("username");
        Optional<Token> tokenValueOpt = tokenRepository.findByUser(username);

        if (!tokenValueOpt.isPresent()) {
            Token tokenObj = new Token();
            tokenObj.setUser(username);
            tokenObj.setToken(token.getToken());
            tokenRepository.save(tokenObj);
        }
    }

    /**
     * Load tokens stored in DB
     * @param request Sample request
     * @return
     */
    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        log.info("Inside custom loadToken()");
        Optional<Token> tokenOpt = Optional.empty();
        String user = request.getParameter("username");
        if (Objects.nonNull(user)) {
            tokenOpt = tokenRepository.findByUser(user);
        } else if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }
            tokenOpt = tokenRepository.findByUser(username);
        }

        if (tokenOpt.isPresent()) {
            Token tokenValue = tokenOpt.get();
            return new DefaultCsrfToken(
                    "X-CSRF-TOKEN",
                    "_csrf",
                    tokenValue.getToken());
        }
        return  null;
    }
}
