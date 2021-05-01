package org.example.silenum.mockito.infrastructure.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "city")
@EntityListeners(AuditingEntityListener.class)
public class CityEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    // Note: This is the owning site (child) of the relation canton-city
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cantonId")
    private CantonEntity canton;

    public CityEntity() {
        // Default constructor is required for JPA.
    }

    private CityEntity(Builder builder) {
        super(builder);
        name = builder.name;
        canton = builder.canton;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(CityEntity copy) {
        Builder builder = new Builder();
        builder.id(copy.getId());
        builder.version(copy.getVersion());
        builder.created(copy.getCreated());
        builder.updated(copy.getUpdated());
        builder.name = copy.getName();
        builder.canton = copy.getCanton();
        return builder;
    }

    public String getName() {
        return name;
    }

    public CantonEntity getCanton() {
        return canton;
    }

    public static final class Builder extends BaseEntity.Builder<Builder> {

        private String name;
        private CantonEntity canton;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder canton(CantonEntity canton) {
            this.canton = canton;
            return this;
        }

        public CityEntity build() {
            return new CityEntity(this);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

    }

}
