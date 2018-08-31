package io.reflectoring.objectmother;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class InvoiceItem {

	private long amount;

	private long price;

	private String productName;

	private double taxFactor;

}
