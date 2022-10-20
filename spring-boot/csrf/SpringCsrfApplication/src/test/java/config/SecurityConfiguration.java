package config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
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

@TestConfiguration
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
                .permitAll().and().httpBasic().and().formLogin();
                //.and().csrf().disable();
                //.and()
                //.csrf()
                //.csrf().csrfTokenRepository(csrfTokenRepository())
                //.ignoringAntMatchers("/login");
                //.requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/login"))
                //.and().csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/home"))
                //.and().csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/registerEmail"));
                //.and()
                //.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
                //.httpBasic().and().formLogin().permitAll();
    }



}
