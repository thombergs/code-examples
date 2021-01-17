/**
 * 
 */
package io.pratik.graphqldemo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Pratik Das
 *
 */

@Repository
public class ProductRepository  {

	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ManufacturerRepository manufacturerRepository;


	public ProductRepository(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
		this.jdbcTemplate =  new JdbcTemplate(dataSource);
	}

	@PostConstruct
	private void initialize() {
		insertProducts(getProducts());
	}

	public void insertProduct(final Product product) {
		String sql = "INSERT INTO Product " + "(id, title, description, rating, manufacturer_id) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] { product.getId(), product.getTitle(), product.getDescription(), product.getRating(),product.getManufacturerID() });
	}

	public void insertProducts(List<Product> products) {
		String sql = "INSERT INTO Product " + "(id, title, description, rating, manufacturer_id) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Product product = products.get(i);
				ps.setString(1, product.getId());
				ps.setString(2, product.getTitle());
				ps.setString(3, product.getDescription());
				ps.setString(4, product.getRating());
				ps.setString(5, product.getManufacturerID());
			}

			public int getBatchSize() {
				return products.size();
			}
		});

	}
	
	public List<Product> getProducts(){
		Product[] products = new Product[] {
				Product.builder().id(UUID.randomUUID().toString()).title("Samsung TV").description("Samsung Television").manufacturerID("SAMSUNG").build(),
				Product.builder().id(UUID.randomUUID().toString()).title("Macbook Pro 13").description("Macbook pro 13 inch laptop").manufacturerID("APPLE").build(),
				Product.builder().id(UUID.randomUUID().toString()).title("Nokia Phone").description("Nokia phone wide screen").manufacturerID("NOKIA").build(),
				Product.builder().id(UUID.randomUUID().toString()).title("Macbook Pro 15").description("Macbook pro 15 inch laptop").manufacturerID("APPLE").build(),
				Product.builder().id(UUID.randomUUID().toString()).title("Macbook air").description("Macbook air 13 inch laptop").manufacturerID("APPLE").build()
		};
		
		return Arrays.asList(products);
	}
	
	public List<Product> getRecentPurchases(final Integer count) {
		List<Product> products = getAllProducts();
		
		return products.subList(0, 2);
	}
	
	public List<Product> getLastVisitedPurchases(final Integer count) {
		List<Product> products = getAllProducts();
		
		return products.subList(0, count);
	}
	
	public List<Product> getProductsByCategory(final String category) {
		String sql = "SELECT id,title,category,description,manufacturer_id FROM Product WHERE category=?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, category);

		List<Product> result = new ArrayList<Product>();
		for (Map<String, Object> row : rows) {
			
			Manufacturer manufacturer = manufacturerRepository.getManufacturerById((String) row.get("manufacturer_id"));
			result.add(Product.builder()
					.id((String) row.get("id"))
					.category((String) row.get("category"))
					.description((String) row.get("description"))
					.title((String) row.get("title"))
					.madeBy(manufacturer)
					.build());
		}

		return result;	}

	public List<Product> getAllProducts() {
		String sql = "SELECT id,title,category,description,manufacturer_id FROM Product";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

		List<Product> result = new ArrayList<Product>();
		for (Map<String, Object> row : rows) {
			
			Manufacturer manufacturer = manufacturerRepository.getManufacturerById((String) row.get("manufacturer_id"));
			result.add(Product.builder()
					.id((String) row.get("id"))
					.category((String) row.get("category"))
					.description((String) row.get("description"))
					.title((String) row.get("title"))
					.madeBy(manufacturer)
					.build());
		}

		return result;
	}


}