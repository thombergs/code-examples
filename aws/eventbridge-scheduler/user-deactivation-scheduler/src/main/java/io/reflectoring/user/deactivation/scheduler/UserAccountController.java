package io.reflectoring.user.deactivation.scheduler;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @DeleteMapping(value = "/deactivate/{userId}")
    public ResponseEntity<Void> deactivateAccount(@PathVariable final UUID userId) {
        userAccountService.deactivateAccount(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/deactivate/cancel/{userId}")
    public ResponseEntity<Void> cancelAccountDeactivation(@PathVariable final UUID userId) {
        userAccountService.cancelAccountDeactivation(userId);
        return ResponseEntity.noContent().build();
    }

}