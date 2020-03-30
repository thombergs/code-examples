package io.reflectoring.liquibase.adapter.datastore;

import static org.assertj.core.api.Assertions.assertThat;

import io.reflectoring.liquibase.domain.User;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Profiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = UserRepositoryIntegrationTest.Initializer.class)
class UserRepositoryIntegrationTest {

  @ClassRule
  public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
    .withDatabaseName("liquibasedemo").withPassword("demopassword").withUsername("demouser");

  @Autowired
  private UserRepository userRepository;


  @Test
  public void findById_userId100000000_returnsUserDetails() {
    Optional<User> user = userRepository.findById(100000000L);
    assertThat(user.isPresent()).isTrue();
    assertThat(user.get()).hasFieldOrPropertyWithValue("id", 100000000L)
      .hasFieldOrPropertyWithValue("userName", "testUser");
  }

  @Test
  public void findById_userId100000001_returnsUserDetails() {
    Optional<User> user = userRepository.findById(100000001L);
    assertThat(user.isPresent()).isTrue();
    assertThat(user.get()).hasFieldOrPropertyWithValue("id", 100000001L)
      .hasFieldOrPropertyWithValue("userName", "testUser1");
  }

  /**
   * This used to override the database properties in spring environment
   */
  public static class Initializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
      if (configurableApplicationContext.getEnvironment().acceptsProfiles(Profiles.of("docker"))) {
        postgreSQLContainer.start();
        configurableApplicationContext.getEnvironment().getSystemProperties()
          .put("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        configurableApplicationContext.getEnvironment().getSystemProperties()
          .put("spring.datasource.username", postgreSQLContainer.getUsername());
        configurableApplicationContext.getEnvironment().getSystemProperties()
          .put("spring.datasource.password", postgreSQLContainer.getPassword());
      }
    }
  }
}