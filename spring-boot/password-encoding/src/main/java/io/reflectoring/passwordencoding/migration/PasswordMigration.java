package io.reflectoring.passwordencoding.migration;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordMigration {

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> authenticationSuccessListener(
            PasswordEncoder encoder, UserDetailsPasswordService userDetailsPasswordService) {
        return (AuthenticationSuccessEvent event) -> {
            Authentication authentication = event.getAuthentication();
            User user = (User) authentication.getPrincipal();
            String encodedPassword = user.getPassword();
            if (encodedPassword.startsWith("{SHA-1}")) {
                CharSequence clearTextPassword = (CharSequence) authentication.getCredentials();
                String newPassword = encoder.encode(clearTextPassword);
                userDetailsPasswordService.updatePassword(user, newPassword);
            }
            ((UsernamePasswordAuthenticationToken) authentication).eraseCredentials();
        };
    }
}
