package io.reflectoring.boundaries.billing.internal.database.internal;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

interface LineItemRepository extends CrudRepository<LineItemJpaEntity, Long> {

  List<LineItemJpaEntity> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

}
