package org.example.silenum.mockito.domain.entity;

import java.util.Set;

public class Canton extends BaseDomain {

    private final String name;
    private final String abbreviation;
    private final Country country;
    private final Set<City> cities;

    private Canton(Builder builder) {
        super(builder);
        this.name = builder.name;
        this.abbreviation = builder.abbreviation;
        this.country = builder.country;
        this.cities = builder.cities;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Country getCountry() {
        return country;
    }

    public Set<City> getCities() {
        return cities;
    }

    public static class Builder extends BaseDomain.Builder<Builder> {

        private String name;
        private String abbreviation;
        private Country country;
        private Set<City> cities;

        private Builder() {
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAbbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
            return this;
        }

        public Builder setCountry(Country country) {
            this.country = country;
            return this;
        }

        public Builder setCities(Set<City> cities) {
            this.cities = cities;
            return this;
        }

        public Builder of(Canton canton) {
            this.id(canton.getId());
            this.version(canton.getVersion());
            this.created(canton.getCreated());
            this.updated(canton.getUpdated());
            this.name = canton.name;
            this.abbreviation = canton.abbreviation;
            this.country = canton.country;
            this.cities = canton.cities;
            return this;
        }

        public Canton build() {
            return new Canton(this);
        }

    }

}
