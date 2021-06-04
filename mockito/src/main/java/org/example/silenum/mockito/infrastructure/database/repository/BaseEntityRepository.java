package org.example.silenum.mockito.infrastructure.database.repository;

import org.example.silenum.mockito.infrastructure.database.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Intermediate repository for {@link BaseEntity}
 * It defines {@link Long} as default type for the ID of the {@link BaseEntity}
 */
public interface BaseEntityRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

}
