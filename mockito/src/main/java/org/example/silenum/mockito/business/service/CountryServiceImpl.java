package org.example.silenum.mockito.business.service;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import org.example.silenum.mockito.business.exception.ElementNotFoundException;
import org.example.silenum.mockito.domain.entity.Canton;
import org.example.silenum.mockito.domain.entity.Country;
import org.example.silenum.mockito.domain.repository.CountryRepository;

public class CountryServiceImpl implements CountryService {

    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Country with %s does not exist.";

    private final CountryRepository countryRepository;
    private final CantonService cantonService;

    public CountryServiceImpl(
            CountryRepository countryRepository,
            CantonService cantonService) {
        this.countryRepository = countryRepository;
        this.cantonService = cantonService;
    }

    @Override
    public Country save(Country country) throws ElementNotFoundException {
        String exceptionMessage = String.format(EXCEPTION_MESSAGE_TEMPLATE, "ID " + country.getId());
        return countryRepository.save(country)
                .orElseThrow(createSupplierOnElementNotFound(exceptionMessage));
    }

    @Override
    public Country find(Long id) throws ElementNotFoundException {
        String exceptionMessage = String.format(EXCEPTION_MESSAGE_TEMPLATE, "ID " + id);
        return countryRepository.find(id)
                .orElseThrow(createSupplierOnElementNotFound(exceptionMessage));
    }

    @Override
    public void delete(Country domain) {
        countryRepository.delete(domain);
    }

    @Override
    public Supplier<ElementNotFoundException> createSupplierOnElementNotFound(String message) {
        return () -> new ElementNotFoundException(message);
    }

    @Override
    public Country findById(Long id, boolean loadCantons, boolean loadCities) throws ElementNotFoundException {
        String exceptionMessage = String.format(EXCEPTION_MESSAGE_TEMPLATE, "ID " + id);
        Country country = countryRepository.find(id).orElseThrow(createSupplierOnElementNotFound(exceptionMessage));
        return buildCountry(country, loadCantons, loadCities);
    }

    @Override
    public Country findByName(String name, boolean loadCantons, boolean loadCities) throws ElementNotFoundException {
        String exceptionMessage = String.format(EXCEPTION_MESSAGE_TEMPLATE, "name " + name);
        Country country = countryRepository.findByName(name)
                .orElseThrow(createSupplierOnElementNotFound(exceptionMessage));
        return buildCountry(country, loadCantons, loadCities);
    }

    @Override
    public Country findByCode(String code, boolean loadCantons, boolean loadCities) throws ElementNotFoundException {
        String exceptionMessage = String.format(EXCEPTION_MESSAGE_TEMPLATE, "code " + code);
        Country country = countryRepository.findByCode(code)
                .orElseThrow(createSupplierOnElementNotFound(exceptionMessage));
        return buildCountry(country, loadCantons, loadCities);
    }

    private Country buildCountry(Country country, boolean loadCantons, boolean loadCities) {
        Set<Canton> cantons = !loadCantons ?
                Collections.emptySet() :
                cantonService.findAllByCountry(country, loadCities);
        return Country.builder().of(country)
                .setCantons(cantons)
                .build();
    }

}
