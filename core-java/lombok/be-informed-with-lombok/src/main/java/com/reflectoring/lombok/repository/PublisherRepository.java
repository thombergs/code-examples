package com.reflectoring.lombok.repository;

import com.reflectoring.lombok.model.persistence.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
