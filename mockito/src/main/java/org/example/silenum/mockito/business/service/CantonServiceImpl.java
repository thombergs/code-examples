package org.example.silenum.mockito.business.service;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.example.silenum.mockito.business.exception.ElementNotFoundException;
import org.example.silenum.mockito.domain.entity.Canton;
import org.example.silenum.mockito.domain.entity.City;
import org.example.silenum.mockito.domain.entity.Country;
import org.example.silenum.mockito.domain.repository.CantonRepository;

public class CantonServiceImpl implements CantonService {

    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Canton with %s does not exist.";

    private final CantonRepository cantonRepository;
    private final CityService cityService;

    public CantonServiceImpl(
            CantonRepository cantonRepository,
            CityService cityService) {
        this.cantonRepository = cantonRepository;
        this.cityService = cityService;
    }

    @Override
    public Canton save(Canton canton) throws ElementNotFoundException {
        String exceptionMessage = String.format(EXCEPTION_MESSAGE_TEMPLATE, "ID " + canton.getId());
        return cantonRepository.save(canton)
                .orElseThrow(createSupplierOnElementNotFound(exceptionMessage));
    }

    @Override
    public Canton find(Long id) throws ElementNotFoundException {
        String exceptionMessage = String.format(EXCEPTION_MESSAGE_TEMPLATE, "id " + id);
        return cantonRepository.find(id)
                .orElseThrow(createSupplierOnElementNotFound(exceptionMessage));
    }

    @Override
    public void delete(Canton domain) {
        cantonRepository.delete(domain);
    }

    @Override
    public Supplier<ElementNotFoundException> createSupplierOnElementNotFound(String message) {
        return () -> new ElementNotFoundException(message);
    }

    @Override
    public Canton findByAbbreviation(String abbreviation, boolean loadCities) throws ElementNotFoundException {
        String exceptionMessage = String.format(EXCEPTION_MESSAGE_TEMPLATE, "abbreviation " + abbreviation);
        Canton canton = cantonRepository.findByAbbreviation(abbreviation)
                .orElseThrow(createSupplierOnElementNotFound(exceptionMessage));
        return buildCanton(canton, loadCities);
    }

    @Override
    public Canton findByName(String name, boolean loadCities) throws ElementNotFoundException {
        String exceptionMessage = String.format(EXCEPTION_MESSAGE_TEMPLATE, "name " + name);
        Canton canton = cantonRepository.findByName(name)
                .orElseThrow(createSupplierOnElementNotFound(exceptionMessage));
        return buildCanton(canton, loadCities);
    }

    @Override
    public Set<Canton> findAllByCountry(Country country, boolean loadCities) {
        Set<Canton> cantons = cantonRepository.findAllByCountry(country);
        return cantons.stream()
                .map(canton -> Canton.builder().of(canton)
                        .setCities(!loadCities ?
                                Collections.emptySet() :
                                cityService.findAllByCanton(canton))
                        .setCountry(country)
                        .build())
                .collect(Collectors.toSet());
    }

    private Canton buildCanton(Canton canton, boolean loadCities) {
        Set<City> cities = !loadCities ? Collections.emptySet() : cityService.findAllByCanton(canton);
        return Canton.builder().of(canton)
                .setCities(cities)
                .build();
    }

}
