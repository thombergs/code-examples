package org.example.silenum.mockito.business.service;

import java.util.Set;

import org.example.silenum.mockito.business.exception.ElementNotFoundException;
import org.example.silenum.mockito.domain.entity.Canton;
import org.example.silenum.mockito.domain.entity.Country;

public interface CantonService extends BaseService<Canton, ElementNotFoundException> {

    Canton findByAbbreviation(String abbreviation, boolean loadCities) throws ElementNotFoundException;

    default Canton findByAbbreviation(String abbreviation) throws ElementNotFoundException {
        return findByAbbreviation(abbreviation, false);
    }

    Canton findByName(String name, boolean loadCities) throws ElementNotFoundException;

    default Canton findByName(String name) throws ElementNotFoundException {
        return findByName(name, false);
    }

    Set<Canton> findAllByCountry(Country country, boolean loadCities);

    default Set<Canton> findAllByCountry(Country country) {
        return findAllByCountry(country, false);
    }

}
