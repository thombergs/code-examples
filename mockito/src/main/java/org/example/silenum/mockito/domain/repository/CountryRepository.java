package org.example.silenum.mockito.domain.repository;

import java.util.Optional;

import org.example.silenum.mockito.domain.entity.Country;

public interface CountryRepository extends BaseDomainRepository<Country> {

    Optional<Country> findByName(String name);

    Optional<Country> findByCode(String code);

}
