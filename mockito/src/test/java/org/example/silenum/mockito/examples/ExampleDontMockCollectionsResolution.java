package org.example.silenum.mockito.examples;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.example.silenum.mockito.domain.entity.City;
import org.junit.jupiter.api.Test;
import org.unitils.reflectionassert.ReflectionAssert;

public class ExampleDontMockCollectionsResolution {

    @Test
    void mockListResolution() {
        List<City> cities = new ArrayList<>();

        City city = createCity();
        City anotherCity = createCity();

        cities.add(city);
        cities.add(anotherCity);

        ReflectionAssert.assertReflectionEquals(city, cities.get(0));
        ReflectionAssert.assertReflectionEquals(anotherCity, cities.get(1));
    }

    private City createCity() {
        return City.builder()
                .id(System.currentTimeMillis())
                .version(ThreadLocalRandom.current().nextInt())
                .created(ZonedDateTime.now().minusDays(1L))
                .updated(ZonedDateTime.now())
                .setName("Swiss Test City " + System.currentTimeMillis())
                .build();
    }

}
