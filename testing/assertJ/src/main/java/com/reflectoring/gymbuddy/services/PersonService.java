package com.reflectoring.gymbuddy.services;

import com.reflectoring.gymbuddy.domain.Person;
import com.reflectoring.gymbuddy.dto.person.PersonAddRequest;
import com.reflectoring.gymbuddy.dto.person.PersonUpdateRequest;
import java.util.List;

public interface PersonService {

    Person add(PersonAddRequest request);

    Person update(String email, PersonUpdateRequest request);

    Person get(String email);

    List<Person> getAll();

    void delete(String email);

    List<Person> getFriends(String email);

    Person addFriend(String email, String friendEmail);

    Person deleteFriend(String email, String friendEmail);

    Person getFriend(String email, String friendEmail);
}
