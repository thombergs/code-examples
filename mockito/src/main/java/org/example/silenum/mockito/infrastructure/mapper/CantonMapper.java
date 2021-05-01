package org.example.silenum.mockito.infrastructure.mapper;

import java.util.Optional;

import org.example.silenum.mockito.domain.entity.Canton;
import org.example.silenum.mockito.infrastructure.database.entity.CantonEntity;

public final class CantonMapper implements Mapper<Canton, CantonEntity> {

    private final CountryMapper countryMapper;

    public CantonMapper(CountryMapper countryMapper) {
        this.countryMapper = countryMapper;
    }

    @Override
    public Optional<Canton> toDomain(CantonEntity entity) {
        return entity == null ? Optional.empty() : Optional.of(Canton.builder()
                .id(entity.getId())
                .version(entity.getVersion())
                .created(entity.getCreated())
                .updated(entity.getUpdated())
                .setName(entity.getName())
                .setAbbreviation(entity.getAbbreviation())
                .build());
    }

    @Override
    public CantonEntity toEntity(Canton canton) {
        return canton == null ? null : CantonEntity.builder()
                .id(canton.getId())
                .version(canton.getVersion())
                .created(canton.getCreated())
                .updated(canton.getUpdated())
                .name(canton.getName())
                .abbreviation(canton.getAbbreviation())
                .country(countryMapper.toEntity(canton.getCountry()))
                .build();
    }

}
