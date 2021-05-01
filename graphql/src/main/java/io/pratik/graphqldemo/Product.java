/**
 * 
 */
package io.pratik.graphqldemo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pratik Das
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Product {
   private String id;	
   private String title;
   private String description; 
   private String rating; 
   private String category;
   private Manufacturer madeBy;
   private String manufacturerID;
	
}
