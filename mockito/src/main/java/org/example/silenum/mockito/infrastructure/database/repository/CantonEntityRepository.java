package org.example.silenum.mockito.infrastructure.database.repository;

import java.util.List;
import java.util.Optional;

import org.example.silenum.mockito.infrastructure.database.entity.CantonEntity;

public interface CantonEntityRepository extends BaseEntityRepository<CantonEntity> {

    Optional<CantonEntity> findByNameIgnoreCase(String name);

    Optional<CantonEntity> findByAbbreviationIgnoreCase(String abbreviation);

    List<CantonEntity> findAllByCountryNameIgnoreCase(String countryName);

    List<CantonEntity> findAllByCountryId(Long countryId);

}
