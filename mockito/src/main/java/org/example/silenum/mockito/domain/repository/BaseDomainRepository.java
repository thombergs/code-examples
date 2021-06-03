package org.example.silenum.mockito.domain.repository;

import java.util.Optional;

import org.example.silenum.mockito.domain.entity.BaseDomain;

public interface BaseDomainRepository<D extends BaseDomain> {

    Optional<D> save(D domain);

    Optional<D> find(Long id);

    void delete(D domain);

}
