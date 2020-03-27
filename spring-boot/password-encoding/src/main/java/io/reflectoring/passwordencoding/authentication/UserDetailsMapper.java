package io.reflectoring.passwordencoding.authentication;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
class UserDetailsMapper {

  UserDetails toUserDetails(UserCredentials userCredentials) {

    return User.withUsername(userCredentials.getUsername())
        .password(userCredentials.getPassword())
        .roles(userCredentials.getRoles().toArray(String[]::new))
        .build();
  }
}
