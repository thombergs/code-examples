package io.reflectoring.objectmother;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ObjectMotherClient {

	@Test
	void invoiceWithAbroadAddress() {
		Invoice invoice = InvoiceMother.complete()
						.address(AddressMother.abroad()
										.build())
						.build();
		assertThat(invoice.getAddress().getCountry()).isEqualTo("DE");
	}

	@Test
	void invoiceWithMissingHouseNumber() {
		Invoice invoice = InvoiceMother.complete()
						.address(AddressMother.complete()
										.houseNumber(null)
										.build())
						.build();
		assertThat(invoice.getAddress().getHouseNumber()).isNull();
	}

	@Test
	void invoiceWithNegativeTotal() {
		Invoice invoice = InvoiceMother.refund()
						.build();
		double sum = invoice.getItems().stream()
						.mapToDouble(item -> item.getAmount() * item.getPrice())
						.sum();
		assertThat(sum).isNegative();
	}

}
