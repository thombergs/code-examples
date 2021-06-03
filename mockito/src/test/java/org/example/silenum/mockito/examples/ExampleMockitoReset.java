package org.example.silenum.mockito.examples;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.example.silenum.mockito.business.exception.ElementNotFoundException;
import org.example.silenum.mockito.business.service.CityService;
import org.example.silenum.mockito.business.service.CityServiceImpl;
import org.example.silenum.mockito.domain.entity.Canton;
import org.example.silenum.mockito.domain.entity.City;
import org.example.silenum.mockito.domain.entity.Country;
import org.example.silenum.mockito.domain.repository.CityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.unitils.reflectionassert.ReflectionAssert;

public class ExampleMockitoReset {

    // System under Test (SuT)
    private CityService cityService;

    // Mocks
    private CityRepository cityRepository;

    @BeforeEach
    void setUp() {
        cityRepository = Mockito.mock(CityRepository.class);
        cityService = new CityServiceImpl(cityRepository);
    }

    @Test
    void findAndDelete() throws ElementNotFoundException {
        City expected = createCity();
        Mockito.when(cityRepository.find(expected.getId())).thenReturn(Optional.of(expected));
        City actual = cityService.find(expected.getId());
        ReflectionAssert.assertReflectionEquals(expected, actual);
        cityService.delete(expected);
        Mockito.verify(cityRepository).delete(expected);
        Mockito.reset(cityRepository);
        Mockito.when(cityRepository.find(expected.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ElementNotFoundException.class, () -> cityService.find(expected.getId()));
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
