package io.reflectoring.objectmother;

import java.util.Collections;

class InvoiceMother {

	static Invoice.InvoiceBuilder complete() {
		return Invoice.builder()
						.id(42L)
						.address(AddressMother.complete()
										.build())
						.items(Collections.singletonList(
										InvoiceItemMother.complete()
														.build()));
	}

	static Invoice.InvoiceBuilder refund() {
		return Invoice.builder()
						.id(42L)
						.address(AddressMother.complete()
										.build())
						.items(Collections.singletonList(
										InvoiceItemMother.withNegativePrice()
														.build()));
	}


}
