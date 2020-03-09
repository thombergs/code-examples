package io.reflectoring.boundaries.billing.api;

import java.time.LocalDate;

public interface InvoiceCalculator {

  Invoice calculateInvoice(Long userId, LocalDate fromDate, LocalDate toDate);

}
