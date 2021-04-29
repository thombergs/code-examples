package org.example.silenum.mockito.domain.entity;

import java.time.ZonedDateTime;

/**
 * This {@link BaseDomain} defines the common fields of all domain objects.
 * Every domain object must extends this {@link BaseDomain} for further processing.
 */
public abstract class BaseDomain {

    private final Long id;
    private final Integer version;
    private final ZonedDateTime created;
    private final ZonedDateTime updated;

    protected BaseDomain(Builder<?> builder) {
        id = builder.id;
        version = builder.version;
        created = builder.created;
        updated = builder.updated;
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

    /**
     * Abstract Builder Pattern for Inheritance
     *
     * @param <T> Builder from Subtype
     */
    public abstract static class Builder<T extends Builder<T>> {

        private Long id;
        private Integer version;
        private ZonedDateTime created;
        private ZonedDateTime updated;

        protected Builder() {
        }

        public T id(Long id) {
            this.id = id;
            return getThis();
        }

        public T version(Integer version) {
            this.version = version;
            return getThis();
        }

        public T created(ZonedDateTime created) {
            this.created = created;
            return getThis();
        }

        public T updated(ZonedDateTime updated) {
            this.updated = updated;
            return getThis();
        }

        /**
         * Returns the this-object of the subtype.
         *
         * @return this-object of subtype
         */
        protected abstract T getThis();

    }

}
