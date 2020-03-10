package io.reflectoring;

import io.reflectoring.api.UserApiDelegate;
import io.reflectoring.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
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

        return ResponseEntity.ok(user);
    }
}
