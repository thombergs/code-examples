package io.reflectoring.javaconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Value("${security.api.key}")
    private String apiKey;

    @Bean("http-security")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/employees/**").hasAuthority("USER")
                .requestMatchers("/departments/**").hasAuthority("ADMIN")
                .requestMatchers("/organizations/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
