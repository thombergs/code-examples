package org.example.silenum.mockito.domain.entity;

import java.util.Set;

public class Country extends BaseDomain {

    private final String name;
    private final String code;
    private final Set<Canton> cantons;

    private Country(Builder builder) {
        super(builder);
        this.name = builder.name;
        this.code = builder.code;
        this.cantons = builder.cantons;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Set<Canton> getCantons() {
        return cantons;
    }

    public static class Builder extends BaseDomain.Builder<Builder> {

        private String name;
        private String code;
        private Set<Canton> cantons;

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

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Builder setCantons(Set<Canton> cantons) {
            this.cantons = cantons;
            return this;
        }

        public Builder of(Country country) {
            this.id(country.getId());
            this.version(country.getVersion());
            this.created(country.getCreated());
            this.updated(country.getUpdated());
            this.name = country.name;
            this.code = country.code;
            this.cantons = country.cantons;
            return this;
        }

        public Country build() {
            return new Country(this);
        }

    }

}
