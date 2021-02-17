/**
 * 
 */
package io.pratik.elasticsearch.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pratik Das
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "productindex")
public class Product {
	@Id
    private String id;
	
	@Field(type = FieldType.Text, name = "name")
	private String name;
	
	@Field(type = FieldType.Double, name = "price")
	private Double price;
	
	@Field(type = FieldType.Integer, name = "quantity")
	private Integer quantity;
	
	@Field(type = FieldType.Keyword, name = "category")
	private String category;
	
	@Field(type = FieldType.Text, name = "desc")
	private String description;
	
	@Field(type = FieldType.Keyword, name = "manufacturer")
	private String manufacturer;

	

}
