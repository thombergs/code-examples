package io.reflectoring.boundaries.billing.internal.database.internal;

import io.reflectoring.boundaries.billing.internal.database.api.LineItem;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
class LineItemJpaEntity {

  @Id
  @GeneratedValue
  private Long userId;
  private String name;
  private Double amount;
  private LocalDate date;

  static LineItemJpaEntity fromDomainObject(LineItem lineItem) {
    return new LineItemJpaEntity(
        lineItem.getUserId(),
        lineItem.getName(),
        lineItem.getAmount(),
        lineItem.getDate()
    );
  }

  LineItem toDomainObject(){
    return new LineItem(
        this.userId,
        this.name,
        this.amount,
        this.date
    );
  }

}
