package io.reflectoring.boundaries.billingmodule.api;

import java.time.LocalDate;

public interface InvoiceCalculator {

  Invoice calculateInvoice(Long userId, LocalDate fromDate, LocalDate toDate);

}
