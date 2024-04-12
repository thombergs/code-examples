package com.reflectoring.security.service;

import com.reflectoring.security.config.UserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserProperties userProperties;

    @Autowired
    public AuthUserDetailsService(UserProperties userProperties) {
        this.userProperties = userProperties;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (StringUtils.isEmpty(username) || !username.equals(userProperties.getName())) {
            throw new UsernameNotFoundException(String.format("User not found, or unauthorized %s", username));
        }

        return new User(userProperties.getName(), userProperties.getPassword(), new ArrayList<>());
    }
}
