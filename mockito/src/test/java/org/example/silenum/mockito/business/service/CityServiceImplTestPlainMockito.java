package org.example.silenum.mockito.business.service;

import org.example.silenum.mockito.domain.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class CityServiceImplTestPlainMockito {

    @BeforeEach
    void setUp() {
        CityRepository cityRepository = Mockito.mock(CityRepository.class);
        CityService cityService = new CityServiceImpl(cityRepository);
    }

}
