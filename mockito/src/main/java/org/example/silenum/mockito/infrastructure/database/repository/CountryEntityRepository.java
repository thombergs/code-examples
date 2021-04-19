package org.example.silenum.mockito.infrastructure.database.repository;

import java.util.Optional;

import org.example.silenum.mockito.infrastructure.database.entity.CountryEntity;

public interface CountryEntityRepository extends BaseEntityRepository<CountryEntity> {

    Optional<CountryEntity> findByNameIgnoreCase(String name);

    Optional<CountryEntity> findByCodeIgnoreCase(String code);

}
