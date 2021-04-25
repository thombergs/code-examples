package org.example.silenum.mockito.business.service;

import java.util.Set;

import org.example.silenum.mockito.business.exception.ElementNotFoundException;
import org.example.silenum.mockito.domain.entity.Canton;
import org.example.silenum.mockito.domain.entity.City;
import org.example.silenum.mockito.domain.entity.Country;

public interface CityService extends BaseService<City, ElementNotFoundException> {

    City findByName(String name) throws ElementNotFoundException;

    Set<City> findAllByCanton(Canton canton);

    Set<City> findAllByCountry(Country country);

}
