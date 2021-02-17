/**
 * 
 */
package io.pratik.graphqldemo;

import org.springframework.stereotype.Service;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

/**
 * @author Pratik Das
 *
 */
@Service
public class Mutation implements GraphQLMutationResolver{

	public Product addRecentProduct(final String title, final String description, final String category) {
		
		return Product.builder().title("television").category("electronic").build();
	}
}
