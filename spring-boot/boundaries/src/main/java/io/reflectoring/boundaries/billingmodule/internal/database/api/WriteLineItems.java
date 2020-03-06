package io.reflectoring.boundaries.billingmodule.internal.database.api;

import java.util.List;

public interface WriteLineItems {

  void saveLineItems(List<LineItem> lineItems);

}
