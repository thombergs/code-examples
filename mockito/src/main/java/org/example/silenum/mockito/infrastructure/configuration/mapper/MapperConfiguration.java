package org.example.silenum.mockito.infrastructure.configuration.mapper;

import org.example.silenum.mockito.infrastructure.mapper.CantonMapper;
import org.example.silenum.mockito.infrastructure.mapper.CityMapper;
import org.example.silenum.mockito.infrastructure.mapper.CountryMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public CityMapper cityMapper(CantonMapper cantonMapper) {
        return new CityMapper(cantonMapper);
    }

    @Bean
    public CantonMapper cantonMapper(CountryMapper countryMapper) {
        return new CantonMapper(countryMapper);
    }

    @Bean
    public CountryMapper countryMapper() {
        return new CountryMapper();
    }

}
