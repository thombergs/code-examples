/**
 * 
 */
package io.pratik.graphqldemo;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coxautodev.graphql.tools.GraphQLResolver;

/**
 * @author Pratik Das
 *
 */
@Service
public class ProductResolver implements GraphQLResolver<Product>{

	private ManufacturerRepository manufacturerRepository;
	
	@Autowired
	public ProductResolver(ManufacturerRepository manufacturerRepository) {
		super();
		this.manufacturerRepository = manufacturerRepository;
	}


	public Manufacturer getMadeBy(final Product product) {
		return manufacturerRepository.getManufacturerById(product.getManufacturerID());
	}
}
