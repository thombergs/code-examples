package com.reflectoring.csrf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] patterns = new String[] {
                "/favicon.ico",
                "/login"
        };
        http
                .authorizeRequests().antMatchers("/**")
                .permitAll().and().httpBasic().and().formLogin()
                .successHandler(loginSuccessHandler())
                //.and().csrf().disable();
                .and()
                .csrf()
                //.csrf().csrfTokenRepository(csrfTokenRepository())
                //.ignoringAntMatchers(patterns);
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/login"))
                .and().csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/home"))
                .and().csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/registerEmail"));
                //.and()
                //.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
                //.httpBasic().and().formLogin().permitAll();
    }

    public AuthenticationSuccessHandler loginSuccessHandler() {
        //return (request, response, authentication) -> response.sendRedirect("/");
        return (request, response, authentication)-> {
            response.sendRedirect("/home");
        };
    }

    // This is just a sample method. Any additional logic required after CsrfFilter approves the request can be added here.
    private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    /*@Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new CustomCsrfTokenRepository();
    }*/

    /*private CookieCsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }*/

}
