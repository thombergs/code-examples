package io.reflectoring.security;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "io.reflectoring.security.enabled", havingValue = "true")
public class SecurityModuleConfiguration extends WebSecurityConfigurerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(SecurityModuleConfiguration.class);

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
            .antMatchers("/resources/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/public/**")
            .permitAll()
            .anyRequest()
            .hasRole("USER")
            .and()
            .formLogin()
            .permitAll();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
            .withUser("user")
            .password("password")
            .roles("USER")
            .and()
            .withUser("admin")
            .password("password")
            .roles("USER", "ADMIN");
  }

  @PostConstruct
  public void postConstruct(){
    logger.info("SECURITY MODULE LOADED!");
  }


}
