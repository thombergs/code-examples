package org.example.silenum.mockito.business.service;

import org.example.silenum.mockito.business.exception.ElementNotFoundException;
import org.example.silenum.mockito.domain.entity.Country;

public interface CountryService extends BaseService<Country, ElementNotFoundException> {

    Country findById(Long id, boolean loadCantons, boolean loadCities) throws ElementNotFoundException;

    default Country findById(Long id, boolean loadCantons) throws ElementNotFoundException {
        return findById(id, loadCantons, false);
    }

    default Country findById(Long id) throws ElementNotFoundException {
        return findById(id, false, false);
    }

    Country findByName(String name, boolean loadCantons, boolean loadCities) throws ElementNotFoundException;

    default Country findByName(String name, boolean loadCantons) throws ElementNotFoundException {
        return findByName(name, loadCantons, false);
    }

    default Country findByName(String name) throws ElementNotFoundException {
        return findByName(name, false, false);
    }

    Country findByCode(String code, boolean loadCantons, boolean loadCities) throws ElementNotFoundException;

    default Country findByCode(String code, boolean loadCantons) throws ElementNotFoundException {
        return findByCode(code, loadCantons, false);
    }

    default Country findByCode(String code) throws ElementNotFoundException {
        return findByCode(code, false, false);
    }

}
