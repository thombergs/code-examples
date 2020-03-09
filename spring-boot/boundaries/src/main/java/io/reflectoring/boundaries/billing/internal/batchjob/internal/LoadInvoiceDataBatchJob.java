package io.reflectoring.boundaries.billing.internal.batchjob.internal;

import io.reflectoring.boundaries.billing.internal.database.api.WriteLineItems;
import io.reflectoring.boundaries.billing.internal.database.api.LineItem;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class LoadInvoiceDataBatchJob {

  private static final Logger logger = LoggerFactory.getLogger(LoadInvoiceDataBatchJob.class);
  private static final Random random = new Random();
  private final WriteLineItems writeLineItems;

  @Scheduled(fixedRate = 5000)
  void loadDataFromBillingSystem() {
    List<LineItem> items = getLineItemsFromBillingSystem();
    writeLineItems.saveLineItems(items);
  }

  private List<LineItem> getLineItemsFromBillingSystem() {
    // imagine this list is loaded from a 3rd party system
    return Arrays.asList(
        lineItem("bread"),
        lineItem("butter"),
        lineItem("toilet paper")
    );
  }

  private LineItem lineItem(String itemName) {
    return new LineItem(42L, itemName, 42.42d, LocalDate.now().minusDays(random.nextInt(10)));
  }

}
