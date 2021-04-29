package org.example.silenum.mockito.examples;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.example.silenum.mockito.domain.entity.City;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.unitils.reflectionassert.ReflectionAssert;

public class ExampleDontMockCollections {

    @Test
    void mockList() {
        List<City> cities = Mockito.mock(List.class);

        City city = createCity();
        City anotherCity = createCity();

        Mockito.when(cities.get(0)).thenReturn(city);
        Mockito.when(cities.get(1)).thenReturn(anotherCity);

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
