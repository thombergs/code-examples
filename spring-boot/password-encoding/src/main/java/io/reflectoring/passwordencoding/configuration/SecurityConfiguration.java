package io.reflectoring.passwordencoding.configuration;

import io.reflectoring.passwordencoding.authentication.JdbcUserDetailPasswordService;
import io.reflectoring.passwordencoding.authentication.JdbcUserDetailsService;
import io.reflectoring.passwordencoding.authentication.UserDetailsMapper;
import io.reflectoring.passwordencoding.authentication.UserRepository;
import io.reflectoring.passwordencoding.workfactor.BcCryptWorkFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final UserRepository userRepository;
  private final UserDetailsMapper userDetailsMapper;
  private final BcCryptWorkFactorService bcCryptWorkFactorService;

  public SecurityConfiguration(
      UserRepository userRepository,
      UserDetailsMapper userDetailsMapper,
      BcCryptWorkFactorService bcCryptWorkFactorService) {
    this.userRepository = userRepository;
    this.userDetailsMapper = userDetailsMapper;
    this.bcCryptWorkFactorService = bcCryptWorkFactorService;
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/registration")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .httpBasic();

    httpSecurity.headers().frameOptions().disable();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider()).eraseCredentials(false);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // we must user deprecated encoder to support their encoding
    String encodingId = "bcrypt";
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put(
        encodingId, new BCryptPasswordEncoder(bcCryptWorkFactorService.calculateStrength()));
    encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
    encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
    encoders.put(
        "MD5",
        new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
    encoders.put(
        "noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
    encoders.put("scrypt", new SCryptPasswordEncoder());
    encoders.put(
        "SHA-1",
        new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
    encoders.put(
        "SHA-256",
        new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
    encoders.put(
        "sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
    encoders.put("argon2", new Argon2PasswordEncoder());

    return new DelegatingPasswordEncoder(encodingId, encoders);
  }

  @Bean
  public UserDetailsPasswordService userDetailsPasswordService() {
    return new JdbcUserDetailPasswordService(userRepository, userDetailsMapper);
  }

  public UserDetailsService userDetailsService() {
    return new JdbcUserDetailsService(userRepository, userDetailsMapper);
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    daoAuthenticationProvider.setUserDetailsPasswordService(userDetailsPasswordService());
    daoAuthenticationProvider.setUserDetailsService(userDetailsService());
    return daoAuthenticationProvider;
  }
}
