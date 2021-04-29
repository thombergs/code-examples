package org.example.silenum.mockito.domain.entity;

public class City extends BaseDomain {

    private final String name;
    private final Canton canton;

    private City(Builder builder) {
        super(builder);
        this.name = builder.name;
        this.canton = builder.canton;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public Canton getCanton() {
        return canton;
    }

    public static class Builder extends BaseDomain.Builder<Builder> {

        private String name;
        private Canton canton;

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

        public Builder setCanton(Canton canton) {
            this.canton = canton;
            return this;
        }

        public Builder of(City city) {
            this.id(city.getId());
            this.version(city.getVersion());
            this.created(city.getCreated());
            this.updated(city.getUpdated());
            this.name = city.name;
            this.canton = city.canton;
            return this;
        }

        public City build() {
            return new City(this);
        }

    }

}
