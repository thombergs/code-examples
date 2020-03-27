package io.reflectoring.passwordencoding.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DatabaseUserDetailPasswordService implements UserDetailsPasswordService {

  private final UserRepository userRepository;

  private final UserDetailsMapper userDetailsMapper;

  public DatabaseUserDetailPasswordService(
      UserRepository userRepository, UserDetailsMapper userDetailsMapper) {
    this.userRepository = userRepository;
    this.userDetailsMapper = userDetailsMapper;
  }

  @Override
  public UserDetails updatePassword(UserDetails user, String newPassword) {
    UserCredentials userCredentials = userRepository.findByUsername(user.getUsername());
    userCredentials.setPassword(newPassword);
    return userDetailsMapper.toUserDetails(userCredentials);
  }
}
