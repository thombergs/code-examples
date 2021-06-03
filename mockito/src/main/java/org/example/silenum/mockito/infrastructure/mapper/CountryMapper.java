package org.example.silenum.mockito.infrastructure.mapper;

import java.util.Optional;

import org.example.silenum.mockito.domain.entity.Country;
import org.example.silenum.mockito.infrastructure.database.entity.CountryEntity;

public final class CountryMapper implements Mapper<Country, CountryEntity> {

    @Override
    public Optional<Country> toDomain(CountryEntity entity) {
        return entity == null ? Optional.empty() : Optional.of(Country.builder()
                .id(entity.getId())
                .version(entity.getVersion())
                .created(entity.getCreated())
                .updated(entity.getUpdated())
                .setName(entity.getName())
                .setCode(entity.getCode())
                .build());
    }

    @Override
    public CountryEntity toEntity(Country domain) {
        return domain == null ? null : CountryEntity.builder()
                .id(domain.getId())
                .version(domain.getVersion())
                .created(domain.getCreated())
                .updated(domain.getUpdated())
                .name(domain.getName())
                .code(domain.getCode())
                .build();
    }

}
