package com.reflectoring.library.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableConfigurationProperties(BasicAuthConfigProperties.class)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final BasicAuthConfigProperties basicAuth;

    public SecurityConfiguration(BasicAuthConfigProperties basicAuth) {
        this.basicAuth = basicAuth;
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests(t -> t.anyRequest().authenticated())
                .httpBasic().and()
                .addFilter(new BasicAuthenticationFilter(authenticationManager(),
                        authErrorHandler()))
                .formLogin().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authErrorHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("Configure basic auth");
        auth.inMemoryAuthentication()
                .withUser(basicAuth.getUsername())
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder()
                        .encode(basicAuth.getPassword()))
                .roles("USER");
    }

    @Bean
    public AuthenticationErrorHandler authErrorHandler() {
        System.out.println("Configure auth Error");
        AuthenticationErrorHandler authenticationErrorHandler = new AuthenticationErrorHandler();
        authenticationErrorHandler.setRealmName("Basic Authentication");
        return authenticationErrorHandler;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type"));
        configuration.setExposedHeaders(Arrays.asList("X-Get-Header"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
