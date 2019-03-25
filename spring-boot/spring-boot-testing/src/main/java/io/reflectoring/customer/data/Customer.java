package io.reflectoring.customer.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	@GeneratedValue
	private Long id;

	@Version
	private Long version;

	private String name;
}
