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

    jdbcTemplate.execute("insert into auth_user values(1, 'reflectoring')");

    final List<AuthUser> authUsers = jdbcTemplate
        .query("SELECT id, username FROM auth_user", (rs, rowNum) -> new AuthUser(
            rs.getString("id"),
            rs.getString("username")
        ));

    Assertions.assertThat(authUsers).isNotEmpty();
  }

  private static class AuthUser {
    public String id;
    public String username;

    public AuthUser(final String id, final String username) {

      this.id = id;
      this.username = username;
    }
  }
}