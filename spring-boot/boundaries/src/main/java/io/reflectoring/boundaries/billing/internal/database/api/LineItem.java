package io.reflectoring.boundaries.billing.internal.database.api;

import java.time.LocalDate;
import lombok.Value;

@Value
public class LineItem {

  private final Long userId;
  private final String name;
  private final double amount;
  private final LocalDate date;

}
