/**
 * 
 */
package io.pratik.graphqldemo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author Pratik Das
 *
 */

@Repository
public class ManufacturerRepository  {

	
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	
	@Autowired
	public ManufacturerRepository(final DataSource dataSource) {
		super();
		this.dataSource = dataSource;
		this.jdbcTemplate =  new JdbcTemplate(dataSource);
	}



	@PostConstruct
	private void initialize() {
		insertManufacturers(getManufacturers());
	}



	public void insertManufacturers(final List<Manufacturer> manufacturers) {
		String sql = "INSERT INTO MANUFACTURER " + "(id, name, address) VALUES (?, ?, ?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Manufacturer manufacturer = manufacturers.get(i);
				ps.setString(1, manufacturer.getId());
				ps.setString(2, manufacturer.getName());
				ps.setString(3, manufacturer.getAddress());
			}

			@Override
			public int getBatchSize() {
				return manufacturers.size();
			}

		});

	}
	
	public List<Manufacturer> getManufacturers(){
		Manufacturer[] manufs = new Manufacturer[] {
				Manufacturer.builder().id("SAMSUNG").name("Samsung").address("Seocho-daero, Seoul, Korea").build(),
				Manufacturer.builder().id("APPLE").name("Apple Inc").address("Cupertino, California, USA").build(),
				Manufacturer.builder().id("NOKIA").name("Nokia").address("Espoo, Finland").build()

		};
		
		return Arrays.asList(manufs);
	}



	
	public Manufacturer getManufacturerById(String manufacturerID) {
		String sql = "SELECT * FROM Manufacturer WHERE id = ?";
		
		return jdbcTemplate.queryForObject(sql, new RowMapper<Manufacturer>() {

			@Override
			public Manufacturer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return Manufacturer.builder()
						.id(rs.getString("id"))
						.name(rs.getString("name"))
						.address(rs.getString("address"))
						.build();
			}
			
		}, new Object[] { manufacturerID });
	}
}