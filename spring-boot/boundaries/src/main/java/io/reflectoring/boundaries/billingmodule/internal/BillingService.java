package io.reflectoring.boundaries.billingmodule.internal;

import io.reflectoring.boundaries.billingmodule.api.Invoice;
import io.reflectoring.boundaries.billingmodule.api.InvoiceCalculator;
import io.reflectoring.boundaries.billingmodule.internal.database.api.LineItem;
import io.reflectoring.boundaries.billingmodule.internal.database.api.ReadLineItems;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class BillingService implements InvoiceCalculator {

  private final ReadLineItems readLineItems;

  @Override
  public Invoice calculateInvoice(Long userId, LocalDate fromDate, LocalDate toDate) {
    List<LineItem> items = readLineItems.getLineItemsForUser(userId, fromDate, toDate);
    double sum = items.stream()
        .mapToDouble(LineItem::getAmount)
        .sum();
    return new Invoice(userId, sum);
  }

}
