package io.reflectoring.testing.persistence;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
class HibernateTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  void injectedComponentsAreNotNull() {
    assertThat(dataSource).isNotNull();
    assertThat(jdbcTemplate).isNotNull();
    assertThat(entityManager).isNotNull();
    assertThat(userRepository).isNotNull();
  }

  @Test
  void stateIsNotShared1() {
    assertThat(userRepository.findByName("user2")).isNull();
    userRepository.save(new UserEntity("user1", "mail1"));
  }

  @Test
  void stateIsNotShared2() {
    assertThat(userRepository.findByName("user1")).isNull();
    userRepository.save(new UserEntity("user2", "mail2"));
  }

  @Test
  void whenSaved_thenFindsByName() {
    userRepository.save(new UserEntity(
            "Zaphod Beeblebrox",
            "zaphod@galaxy.net"));
    assertThat(userRepository.findByName("Zaphod Beeblebrox")).isNotNull();
  }

}