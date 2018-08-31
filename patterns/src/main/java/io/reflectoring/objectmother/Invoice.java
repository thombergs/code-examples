package io.reflectoring.objectmother;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class Invoice {

	private long id;

	private Address address;

	private List<InvoiceItem> items;

}
