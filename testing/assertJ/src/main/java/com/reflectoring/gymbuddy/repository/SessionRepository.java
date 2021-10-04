package com.reflectoring.gymbuddy.repository;

import com.reflectoring.gymbuddy.domain.Session;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {

  List<Session> getAllByPersonEmail(String email);

}
