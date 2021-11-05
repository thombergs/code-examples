package com.reflectoring.gymbuddy.services;

import com.reflectoring.gymbuddy.domain.Person;
import com.reflectoring.gymbuddy.domain.Session;
import com.reflectoring.gymbuddy.dto.session.SessionAddRequest;
import com.reflectoring.gymbuddy.dto.session.SessionUpdateRequest;
import java.util.List;

public interface SessionService {

  Session add(Person person, SessionAddRequest request);

  Session update(long id, SessionUpdateRequest request);

  Session get(long id);

  List<Session> getAll();

  List<Session> getForPerson(String email);

}
