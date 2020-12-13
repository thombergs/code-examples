/**
 * 
 */
package io.pratik.elasticsearch.models;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
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
@Document(indexName = "searchsuggest")
public class SearchSuggest {
	
	@Id
    private String id;
	
	@Field(type = FieldType.Text)
	private String searchText;
	
	@CreatedDate
	@Field(type = FieldType.Date, format = DateFormat.basic_date_time)
	private Instant creationDate; 

}
