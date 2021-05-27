/**
 * 
 */
package io.pratik.springcloudrds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pratikdas
 *
 */
@Service
public class SystemRepository {
	
	 private final JdbcTemplate jdbcTemplate;

	 @Autowired
	 public SystemRepository(DataSource dataSource) {
	 	this.jdbcTemplate = new JdbcTemplate(dataSource);
	 }
	
	public String getCurrentDate() {
		String result = jdbcTemplate.queryForObject("SELECT CURRENT_DATE FROM DUAL", new RowMapper<String>(){

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
			
		});
		return result;
	}
	
	
	@Transactional(readOnly = true)
	public List<String> getUsers(){
		List<String> result = jdbcTemplate.query("SELECT USER() FROM DUAL", new RowMapper<String>(){

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
			
		});
		return result;					
	}

}
