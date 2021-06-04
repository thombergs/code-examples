package org.example.silenum.mockito.infrastructure.database.repository;

import java.util.List;
import java.util.Optional;

import org.example.silenum.mockito.infrastructure.database.entity.CityEntity;

public interface CityEntityRepository extends BaseEntityRepository<CityEntity> {

    Optional<CityEntity> findByNameIgnoreCase(String name);

    List<CityEntity> findAllByCantonId(Long cantonId);

    List<CityEntity> findAllByCanton_Country_Id(Long countryId);

}
