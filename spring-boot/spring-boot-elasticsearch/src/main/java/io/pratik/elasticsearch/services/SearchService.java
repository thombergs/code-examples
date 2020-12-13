/**
 * 
 */
package io.pratik.elasticsearch.services;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import io.pratik.elasticsearch.models.Product;
import io.pratik.elasticsearch.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Pratik Das
 *
 */
@Service
@Slf4j
public class SearchService {
	
	private  ProductRepository productRepository;
	
	private  ElasticsearchOperations elasticsearchOperations;
 
	@Autowired
	public SearchService(ProductRepository productRepository, ElasticsearchOperations elasticsearchOperations) {
		super();
		this.productRepository = productRepository;
		this.elasticsearchOperations = elasticsearchOperations;
	}



	public List<Product> fetchProductNames(final String name){
		
		return productRepository.findByManufacturerAndCategory(name, "");
	}
	
	public List<Product> fetchProductNamesContaining(final String name){
		
		
	      
		return productRepository.findByNameContaining(name);
	}

}
