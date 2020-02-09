package io.reflectoring;

import io.reflectoring.api.UserApi;
import io.reflectoring.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

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
