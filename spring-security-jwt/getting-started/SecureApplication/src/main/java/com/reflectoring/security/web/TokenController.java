package com.reflectoring.security.web;

import com.reflectoring.security.model.TokenRequest;
import com.reflectoring.security.model.TokenResponse;
import com.reflectoring.security.service.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token/create")
    public TokenResponse createToken(@RequestBody TokenRequest tokenRequest) {
        return tokenService.generateToken(tokenRequest);
    }

    @PostMapping("/token/refresh")
    public TokenResponse refreshToken(@RequestAttribute String claims) {
        return tokenService.generateRefreshToken(claims);
    }
}
