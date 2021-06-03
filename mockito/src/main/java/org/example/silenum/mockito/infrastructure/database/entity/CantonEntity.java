package org.example.silenum.mockito.infrastructure.database.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "canton")
@EntityListeners(AuditingEntityListener.class)
public class CantonEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String abbreviation;

    @OneToMany(mappedBy = "canton", cascade = CascadeType.REMOVE)
    private Set<CityEntity> cities = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    private CountryEntity country;

    public CantonEntity() {
        // Default constructor is required for JPA.
    }

    private CantonEntity(Builder builder) {
        super(builder);
        name = builder.name;
        abbreviation = builder.abbreviation;
        cities = builder.cities;
        country = builder.country;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(CantonEntity copy) {
        Builder builder = new Builder();
        builder.id(copy.getId());
        builder.version(copy.getVersion());
        builder.created(copy.getCreated());
        builder.updated(copy.getUpdated());
        builder.name = copy.getName();
        builder.abbreviation = copy.getAbbreviation();
        builder.cities = copy.getCities();
        builder.country = copy.getCountry();
        return builder;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Set<CityEntity> getCities() {
        return cities;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public static final class Builder extends BaseEntity.Builder<Builder> {

        private String name;
        private String abbreviation;
        private Set<CityEntity> cities;
        private CountryEntity country;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder abbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
            return this;
        }

        public Builder cities(Set<CityEntity> cities) {
            this.cities = cities;
            return this;
        }

        public Builder country(CountryEntity country) {
            this.country = country;
            return this;
        }

        public CantonEntity build() {
            return new CantonEntity(this);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

    }

}
