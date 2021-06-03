package org.example.silenum.mockito.infrastructure.database.entity;

import java.time.ZonedDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * This class defines the common fields for each entity to be persisted.
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Version
    private Integer version;

    @CreatedDate
    private ZonedDateTime created;

    @LastModifiedDate
    private ZonedDateTime updated;

    protected BaseEntity() {
    }

    protected BaseEntity(Builder<?> builder) {
        this.id = builder.id;
        this.version = builder.version;
        this.created = builder.created;
        this.updated = builder.updated;
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public abstract static class Builder<T extends Builder<T>> {

        private Long id;
        private Integer version;
        private ZonedDateTime created;
        private ZonedDateTime updated;

        public T id(Long id) {
            this.id = id;
            return this.getThis();
        }

        public T version(Integer version) {
            this.version = version;
            return this.getThis();
        }

        public T created(ZonedDateTime created) {
            this.created = created;
            return this.getThis();
        }

        public T updated(ZonedDateTime updated) {
            this.updated = updated;
            return this.getThis();
        }

        protected abstract T getThis();

    }

}
