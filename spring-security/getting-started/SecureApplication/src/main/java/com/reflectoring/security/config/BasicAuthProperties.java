package com.reflectoring.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "auth")
public class BasicAuthProperties {

    private Map<String, UserDetail> users;

    public Map<String, UserDetail> getUsers() {
        return users;
    }

    public void setUsers(Map<String, UserDetail> users) {
        this.users = users;
    }

    public Set<UserDetails> getUserDetails() {
        return this.users.entrySet().stream()
                .map(entry -> User.withUsername(entry.getKey())
                        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                        .password(entry.getValue().getPassword())
                        .roles(entry.getValue().getRole().toUpperCase())
                        .build())
                .collect(Collectors.toSet());
    }

    private static class UserDetail {

        private String password;

        private String role;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

}
