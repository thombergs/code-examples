package org.example.silenum.mockito.business.service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.example.silenum.mockito.business.exception.ElementNotFoundException;
import org.example.silenum.mockito.domain.entity.Canton;
import org.example.silenum.mockito.domain.entity.City;
import org.example.silenum.mockito.domain.entity.Country;
import org.example.silenum.mockito.domain.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unitils.reflectionassert.ReflectionAssert;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTestMockitoJUnitExtensionStyle {

    // System under Test (SuT)
    private CityService cityService;

    // Mocks
    @Mock
    private CityRepository cityRepository;

    @BeforeEach
    void setUp() {
        cityService = new CityServiceImpl(cityRepository);
    }

    @Test
    void save() throws ElementNotFoundException {
        City expected = createCity();
        Mockito.when(cityRepository.save(expected))
                .thenReturn(Optional.of(expected));
        City actual = cityService.save(expected);
        ReflectionAssert.assertReflectionEquals(expected, actual);
    }

    @Test
    void find() throws ElementNotFoundException {
        City expected = createCity();
        Mockito.when(cityRepository.find(expected.getId()))
                .thenReturn(Optional.of(expected));
        City actual = cityService.find(expected.getId());
        ReflectionAssert.assertReflectionEquals(expected, actual);
    }

    @Test
    void delete() throws ElementNotFoundException {
        City expected = createCity();
        cityService.delete(expected);
        Mockito.verify(cityRepository).delete(expected);
    }

    @Test
    void findByName() throws ElementNotFoundException {
        City expected = createCity();
        Mockito.when(cityRepository.findByName(expected.getName()))
                .thenReturn(Optional.of(expected));
        City actual = cityService.findByName(expected.getName());
        ReflectionAssert.assertReflectionEquals(expected, actual);
    }

    @Test
    void findAllByCanton() {
        City city = createCity();
        Canton canton = city.getCanton();
        Mockito.when(cityRepository.findAllByCanton(canton))
                .thenReturn(Collections.singleton(city));
        Set<City> expected = Set.of(city);
        Set<City> actual = cityService.findAllByCanton(canton);
        ReflectionAssert.assertReflectionEquals(expected, actual);
    }

    @Test
    void findAllByCountry() {
        City city = createCity();
        Country country = city.getCanton().getCountry();
        Mockito.when(cityRepository.findAllByCountry(country))
                .thenReturn(Collections.singleton(city));
        Set<City> expected = Set.of(city);
        Set<City> actual = cityService.findAllByCountry(country);
        ReflectionAssert.assertReflectionEquals(expected, actual);
    }

    private City createCity() {
        return City.builder()
                .id(System.currentTimeMillis())
                .version(ThreadLocalRandom.current().nextInt())
                .created(ZonedDateTime.now().minusDays(1L))
                .updated(ZonedDateTime.now())
                .setName("Swiss Test City " + System.currentTimeMillis())
                .setCanton(createCanton())
                .build();
    }

    private Canton createCanton() {
        return Canton.builder()
                .id(System.currentTimeMillis())
                .version(ThreadLocalRandom.current().nextInt())
                .created(ZonedDateTime.now().minusDays(1L))
                .updated(ZonedDateTime.now())
                .setName("Swiss Canton" + System.currentTimeMillis())
                .setAbbreviation("SC-" + System.currentTimeMillis())
                .setCountry(createCountry())
                .build();
    }

    private Country createCountry() {
        return Country.builder()
                .id(System.currentTimeMillis())
                .version(ThreadLocalRandom.current().nextInt())
                .created(ZonedDateTime.now().minusDays(1L))
                .updated(ZonedDateTime.now())
                .setName("Switzerland")
                .build();
    }

}