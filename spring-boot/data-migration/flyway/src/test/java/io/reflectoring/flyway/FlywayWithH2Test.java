package io.reflectoring.flyway;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
class FlywayWithH2Test {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void databaseHasBeenInitialized() {

    jdbcTemplate.execute("insert into test_user (username, first_name, last_name) values('reflectoring', 'Elvis', 'Presley')");

    final List<AuthUser> authUsers = jdbcTemplate
        .query("SELECT username, first_name, last_name FROM test_user", (rs, rowNum) -> new AuthUser(
            rs.getString("username"),
            rs.getString("first_name"),
            rs.getString("last_name")
        ));

    Assertions.assertThat(authUsers).isNotEmpty();
  }

  private static class AuthUser {
    public String username;
    public String firstName;
    public String lastName;

    public AuthUser(final String username, final String firstName, final String lastName) {

      this.username = username;
      this.firstName = firstName;
      this.lastName = lastName;
    }
  }
}