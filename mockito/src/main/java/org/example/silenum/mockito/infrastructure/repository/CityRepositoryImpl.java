package org.example.silenum.mockito.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.example.silenum.mockito.domain.entity.Canton;
import org.example.silenum.mockito.domain.entity.City;
import org.example.silenum.mockito.domain.entity.Country;
import org.example.silenum.mockito.domain.repository.CityRepository;
import org.example.silenum.mockito.infrastructure.database.entity.CityEntity;
import org.example.silenum.mockito.infrastructure.database.repository.CityEntityRepository;
import org.example.silenum.mockito.infrastructure.mapper.CityMapper;

public class CityRepositoryImpl implements CityRepository {

    private final CityEntityRepository cityEntityRepository;
    private final CityMapper cityMapper;

    public CityRepositoryImpl(
            CityEntityRepository cityEntityRepository,
            CityMapper cityMapper) {
        this.cityEntityRepository = cityEntityRepository;
        this.cityMapper = cityMapper;
    }

    @Override
    public Optional<City> save(City domain) {
        CityEntity cityEntity = cityMapper.toEntity(domain);
        cityEntity = cityEntityRepository.save(cityEntity);
        return cityMapper.toDomain(cityEntity);
    }

    @Override
    public Optional<City> find(Long id) {
        Optional<CityEntity> cityEntity = cityEntityRepository.findById(id);
        return cityMapper.toDomain(cityEntity);
    }

    @Override
    public void delete(City domain) {
        Optional<CityEntity> cantonEntity = cityEntityRepository.findById(domain.getId());
        cantonEntity.ifPresent(cityEntityRepository::delete);
    }

    @Override
    public Optional<City> findByName(String name) {
        Optional<CityEntity> cityEntity = cityEntityRepository.findByNameIgnoreCase(name);
        return cityMapper.toDomain(cityEntity);
    }

    @Override
    public Set<City> findAllByCanton(Canton canton) {
        List<CityEntity> cityEntities = cityEntityRepository.findAllByCantonId(canton.getId());
        return cityMapper.toDomain(cityEntities);
    }

    @Override
    public Set<City> findAllByCountry(Country country) {
        List<CityEntity> cities = cityEntityRepository.findAllByCanton_Country_Id(country.getId());
        return cityMapper.toDomain(cities);
    }

}
