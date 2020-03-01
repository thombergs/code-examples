package io.reflectoring.passwordencoding.configuration;

import io.reflectoring.passwordencoding.authentication.DatabaseUserDetailPasswordService;
import io.reflectoring.passwordencoding.authentication.DatabaseUserDetailsService;
import io.reflectoring.passwordencoding.workfactor.BcCryptWorkFactorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final BcCryptWorkFactorService bcCryptWorkFactorService;
  private final DatabaseUserDetailsService databaseUserDetailsService;
  private final DatabaseUserDetailPasswordService databaseUserDetailPasswordService;

  public SecurityConfiguration(
          BcCryptWorkFactorService bcCryptWorkFactorService,
          DatabaseUserDetailsService databaseUserDetailsService,
          DatabaseUserDetailPasswordService databaseUserDetailPasswordService) {
    this.bcCryptWorkFactorService = bcCryptWorkFactorService;
    this.databaseUserDetailsService = databaseUserDetailsService;
    this.databaseUserDetailPasswordService = databaseUserDetailPasswordService;
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

  @Bean
  public PasswordEncoder passwordEncoder() {
    // we must use deprecated encoder to support their encoding
    String encodingId = "bcrypt";
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put(encodingId, new BCryptPasswordEncoder(bcCryptWorkFactorService.calculateStrength()));
    encoders.put("ldap", new LdapShaPasswordEncoder());
    encoders.put("MD4", new Md4PasswordEncoder());
    encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
    encoders.put("noop", NoOpPasswordEncoder.getInstance());
    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
    encoders.put("scrypt", new SCryptPasswordEncoder());
    encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
    encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
    encoders.put("sha256", new StandardPasswordEncoder());
    encoders.put("argon2", new Argon2PasswordEncoder());

    return new DelegatingPasswordEncoder(encodingId, encoders);
  }

  @Bean
  public AuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsPasswordService(this.databaseUserDetailPasswordService);
    provider.setUserDetailsService(this.databaseUserDetailsService);
    return provider;
  }
}
