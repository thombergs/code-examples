package org.example.silenum.mockito.infrastructure.configuration.repository;

import org.example.silenum.mockito.domain.repository.CantonRepository;
import org.example.silenum.mockito.domain.repository.CityRepository;
import org.example.silenum.mockito.domain.repository.CountryRepository;
import org.example.silenum.mockito.infrastructure.database.repository.CantonEntityRepository;
import org.example.silenum.mockito.infrastructure.database.repository.CityEntityRepository;
import org.example.silenum.mockito.infrastructure.database.repository.CountryEntityRepository;
import org.example.silenum.mockito.infrastructure.mapper.CantonMapper;
import org.example.silenum.mockito.infrastructure.mapper.CityMapper;
import org.example.silenum.mockito.infrastructure.mapper.CountryMapper;
import org.example.silenum.mockito.infrastructure.repository.CantonRepositoryImpl;
import org.example.silenum.mockito.infrastructure.repository.CityRepositoryImpl;
import org.example.silenum.mockito.infrastructure.repository.CountryRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public CityRepository cityRepository(
            CityEntityRepository cityEntityRepository,
            CityMapper cityMapper) {
        return new CityRepositoryImpl(cityEntityRepository, cityMapper);
    }

    @Bean
    public CantonRepository cantonRepository(
            CantonEntityRepository cantonEntityRepository,
            CantonMapper cantonMapper) {
        return new CantonRepositoryImpl(cantonEntityRepository, cantonMapper);
    }

    @Bean
    public CountryRepository countryRepository(
            CountryEntityRepository countryEntityRepository,
            CountryMapper countryMapper) {
        return new CountryRepositoryImpl(countryEntityRepository, countryMapper);
    }

}
