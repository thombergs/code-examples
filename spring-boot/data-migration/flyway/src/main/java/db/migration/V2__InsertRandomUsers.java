package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * Example of a Java-based migration using Spring {@link JdbcTemplate}.
 */
public class V2__InsertRandomUsers extends BaseJavaMigration {

  public void migrate(Context context) {

    final JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));

    // Create 10 random users
    for (int i = 1; i <= 10; i++) {
      jdbcTemplate.execute(String.format("insert into test_user(username, first_name, last_name) "
                                             + "values('%d@reflectoring.io', 'Elvis_%d', 'Presley_%d')", i, i, i));
    }
  }
}