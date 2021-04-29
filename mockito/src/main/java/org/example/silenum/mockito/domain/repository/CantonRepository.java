package org.example.silenum.mockito.domain.repository;

import java.util.Optional;
import java.util.Set;

import org.example.silenum.mockito.domain.entity.Canton;
import org.example.silenum.mockito.domain.entity.Country;

public interface CantonRepository extends BaseDomainRepository<Canton> {

    Optional<Canton> findByAbbreviation(String abbreviation);

    Optional<Canton> findByName(String name);

    Set<Canton> findAllByCountry(Country country);

}
