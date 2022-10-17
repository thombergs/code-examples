package com.reflectoring.timezones.repository;

import com.reflectoring.timezones.model.DateTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateTimeRepository extends JpaRepository<DateTimeEntity, Integer> {
}
