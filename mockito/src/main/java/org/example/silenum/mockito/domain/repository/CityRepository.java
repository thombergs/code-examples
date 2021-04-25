package org.example.silenum.mockito.domain.repository;

import java.util.Optional;
import java.util.Set;

import org.example.silenum.mockito.domain.entity.Canton;
import org.example.silenum.mockito.domain.entity.City;
import org.example.silenum.mockito.domain.entity.Country;

public interface CityRepository extends BaseDomainRepository<City> {

    Optional<City> findByName(String name);

    Set<City> findAllByCanton(Canton canton);

    Set<City> findAllByCountry(Country country);

}
