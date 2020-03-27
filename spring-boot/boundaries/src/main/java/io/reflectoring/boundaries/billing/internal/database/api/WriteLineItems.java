package io.reflectoring.boundaries.billing.internal.database.api;

import java.util.List;

public interface WriteLineItems {

  void saveLineItems(List<LineItem> lineItems);

}
