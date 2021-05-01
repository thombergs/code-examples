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
import org.mockito.Mockito;
import org.unitils.reflectionassert.ReflectionAssert;

class CityServiceImplTestMockitoDonts {

    // System under Test (SuT)
    private CityService cityService;

    // Mocks
    private CityRepository cityRepository;

    // Helper
    private City expected;

    @BeforeEach
    void setUp() {
        expected = createCity();
        cityRepository = Mockito.mock(CityRepository.class);
        cityService = new CityServiceImpl(cityRepository);

        Mockito.when(cityRepository.save(expected))
                .thenReturn(Optional.of(expected));
        Mockito.when(cityRepository.find(expected.getId()))
                .thenReturn(Optional.of(expected));
        Mockito.when(cityRepository.findByName(expected.getName()))
                .thenReturn(Optional.of(expected));
        Mockito.when(cityRepository.findAllByCanton(expected.getCanton()))
                .thenReturn(Collections.singleton(expected));
        Mockito.when(cityRepository.findAllByCountry(expected.getCanton().getCountry()))
                .thenReturn(Collections.singleton(expected));
    }

    @Test
    void save() throws ElementNotFoundException {
        ReflectionAssert.assertReflectionEquals(expected, cityService.save(expected));
    }

    @Test
    void find() throws ElementNotFoundException {
        ReflectionAssert.assertReflectionEquals(expected, cityService.find(expected.getId()));
    }

    @Test
    void delete() throws ElementNotFoundException {
        cityService.delete(expected);
        Mockito.verify(cityRepository).delete(expected);
    }

    @Test
    void findByName() throws ElementNotFoundException {
        ReflectionAssert.assertReflectionEquals(expected, cityService.findByName(expected.getName()));
    }

    @Test
    void findAllByCanton() {
        Set<City> expectedSet = Set.of(expected);
        ReflectionAssert.assertReflectionEquals(expectedSet, cityService.findAllByCanton(expected.getCanton()));
    }

    @Test
    void findAllByCountry() {
        Set<City> expectedSet = Set.of(expected);
        ReflectionAssert.assertReflectionEquals(expectedSet, cityService.findAllByCountry(expected.getCanton().getCountry()));
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