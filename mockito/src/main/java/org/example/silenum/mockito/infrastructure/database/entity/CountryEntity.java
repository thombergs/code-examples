package org.example.silenum.mockito.infrastructure.database.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "country")
@EntityListeners(AuditingEntityListener.class)
public class CountryEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    // Note: This is the inverse site (parent) of the relation country-canton
    @OneToMany(mappedBy = "country", cascade = CascadeType.REMOVE)
    private Set<CantonEntity> cantons = new HashSet<>();

    public CountryEntity() {
        // Default constructor is required for JPA.
    }

    private CountryEntity(Builder builder) {
        super(builder);
        name = builder.name;
        code = builder.code;
        cantons = builder.cantons;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(CountryEntity copy) {
        Builder builder = new Builder();
        builder.id(copy.getId());
        builder.version(copy.getVersion());
        builder.created(copy.getCreated());
        builder.updated(copy.getUpdated());
        builder.name = copy.getName();
        builder.code = copy.getCode();
        builder.cantons = copy.getCantons();
        return builder;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Set<CantonEntity> getCantons() {
        return cantons;
    }

    public static final class Builder extends BaseEntity.Builder<Builder> {

        private String name;
        private String code;
        private Set<CantonEntity> cantons;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder cantons(Set<CantonEntity> cantons) {
            this.cantons = cantons;
            return this;
        }

        public CountryEntity build() {
            return new CountryEntity(this);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

    }

}
