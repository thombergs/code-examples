package com.reflectoring.library.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reflectoring.library.exception.AuthenticationErrorHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(BasicAuthConfigProperties.class)
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
    private static final String URI_PATTERN = "/library/managed/**";

    private final BasicAuthConfigProperties basicAuth;

    public SecurityConfiguration(BasicAuthConfigProperties basicAuth) {
        this.basicAuth = basicAuth;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(URI_PATTERN).permitAll()
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll().and()
                .addFilter(new BasicAuthenticationFilter(authenticationManager(),
                        authenticationErrorHandler()))
                .formLogin().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationErrorHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(basicAuth.getUsername())
                .password(PasswordEncoderFactories
                        .createDelegatingPasswordEncoder()
                        .encode(basicAuth.getPassword()))
                .roles("USER");
    }

    @Bean
    public AuthenticationEntryPoint authenticationErrorHandler() {
        AuthenticationErrorHandler authErrorHandler =
                new AuthenticationErrorHandler(new ObjectMapper());
        authErrorHandler.setRealmName("Library Authentication");
        return authErrorHandler;
    }
}
