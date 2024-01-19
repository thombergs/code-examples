package io.reflectoring.services.impl;

import lombok.extern.slf4j.Slf4j;
import io.reflectoring.api.UserApiDelegate;
import io.reflectoring.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserApiDelegateImpl implements UserApiDelegate {

    @Override
    public ResponseEntity<User> getUserByName(String username) {
        User user = new User();

        user.setId(123L);
        user.setFirstName("Petros");
        user.setLastName("S");
        user.setUsername("Petros");
        user.setEmail("petors.stergioulas94@gmail.com");
        user.setPassword("secret");
        user.setPhone("+123 4567890");
        user.setUserStatus(0);

        log.info("we are the one for now ");
        log.error("we are error ");
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<User> updateUser(String username, User body) {
        return null;
    }

    @Override
    public ResponseEntity<Void> createUser(User body) {
        User user = new User();
        user.setEmail("email");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
