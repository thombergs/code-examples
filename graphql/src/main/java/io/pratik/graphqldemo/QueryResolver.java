/**
 * 
 */
package io.pratik.graphqldemo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

/**
 * @author Pratik Das
 *
 */
@Service
public class QueryResolver implements GraphQLQueryResolver {

	private ProductRepository productRepository;
	
	@Autowired
	public QueryResolver(final ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

	public List<Product> getMyRecentPurchases(final Integer count, String customerID) {
		
		List<Product> products = productRepository.getRecentPurchases(count);
		
		return products;
	}
	
	public List<Product> getLastVisitedProducts(final Integer count, final String customerID) {
		List<Product> products = productRepository.getLastVisitedPurchases(count);
		return products;
	}
	
	public List<Product> getProductsByCategory( final String category) {
		List<Product> products = productRepository.getProductsByCategory(category);
		return products;
	}
	


}
