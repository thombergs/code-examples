package io.reflectoring.objectmother;

class InvoiceItemMother {

	static InvoiceItem.InvoiceItemBuilder complete() {
		return InvoiceItem.builder()
						.amount(1)
						.price(1234L)
						.productName("The Hitchhiker's Guide to the Galaxy")
						.taxFactor(0.19d);
	}

	static InvoiceItem.InvoiceItemBuilder withNegativePrice() {
		return InvoiceItem.builder()
						.amount(1)
						.price(-1234L)
						.productName("The Hitchhiker's Guide to the Galaxy")
						.taxFactor(0.19d);
	}

}
