package com.reflectoring.library.repository;

import com.reflectoring.library.model.persistence.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, String> {
}
