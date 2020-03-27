package io.reflectoring.boundaries.billing.internal;

import io.reflectoring.boundaries.billing.api.Invoice;
import io.reflectoring.boundaries.billing.api.InvoiceCalculator;
import io.reflectoring.boundaries.billing.internal.database.api.LineItem;
import io.reflectoring.boundaries.billing.internal.database.api.ReadLineItems;
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
