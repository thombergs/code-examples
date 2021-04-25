package org.example.silenum.mockito.infrastructure.configuration.business;

import org.example.silenum.mockito.business.service.CantonService;
import org.example.silenum.mockito.business.service.CantonServiceImpl;
import org.example.silenum.mockito.business.service.CityService;
import org.example.silenum.mockito.business.service.CityServiceImpl;
import org.example.silenum.mockito.business.service.CountryService;
import org.example.silenum.mockito.business.service.CountryServiceImpl;
import org.example.silenum.mockito.domain.repository.CantonRepository;
import org.example.silenum.mockito.domain.repository.CityRepository;
import org.example.silenum.mockito.domain.repository.CountryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public CityService cityService(CityRepository cityRepository) {
        return new CityServiceImpl(cityRepository);
    }

    @Bean
    public CantonService cantonService(
            CantonRepository cantonRepository,
            CityService cityService) {
        return new CantonServiceImpl(cantonRepository, cityService);
    }

    @Bean
    public CountryService countryService(
            CountryRepository countryRepository,
            CantonService cantonService) {
        return new CountryServiceImpl(countryRepository, cantonService);
    }

}
