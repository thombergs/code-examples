package io.reflectoring.testing.persistence;

import io.reflectoring.testing.domain.SaveUserPort;
import io.reflectoring.testing.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersistenceAdapter implements SaveUserPort {

  private final UserEntityRepository userEntityRepository;

  @Override
  public Long saveUser(User user) {
    UserEntity userEntity = new UserEntity(
            user.getName(),
            user.getEmail());
    UserEntity savedUserEntity = userEntityRepository.save(userEntity);
    return savedUserEntity.getId();
  }
}
