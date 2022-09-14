package com.reflectoring.csrf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        //http.csrf().disable();
        http.csrf(csrfCustomize -> {
            csrfCustomize.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
            csrfCustomize.ignoringAntMatchers("/disabledEndpoint", "/anotherEndpointParent/**");
        });
    }
}
