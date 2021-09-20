package com.reflectoring.gymbuddy.services.implementation;

import com.reflectoring.gymbuddy.domain.Person;
import com.reflectoring.gymbuddy.domain.Person.PersonBuilder;
import com.reflectoring.gymbuddy.dto.person.PersonAddRequest;
import com.reflectoring.gymbuddy.dto.person.PersonUpdateRequest;
import com.reflectoring.gymbuddy.repository.PersonRepository;
import com.reflectoring.gymbuddy.services.PersonService;
import com.reflectoring.gymbuddy.services.SessionService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

  private PersonRepository personRepository;

  private SessionService sessionService;

  public PersonServiceImpl(PersonRepository personRepository, SessionService sessionService){
    this.personRepository = personRepository;
    this.sessionService = sessionService;
  }

  @Override
  public Person add(PersonAddRequest request) {
    Person person = new PersonBuilder()
        .email(request.getEmail())
        .name(request.getName())
        .lastname(request.getLastname())
        .password(request.getPassword())
        .friends(new HashSet<>())
        .sessions(new ArrayList<>())
        .build();

    person = personRepository.save(person);

    return person;
  }

  @Override
  public Person update(String email, PersonUpdateRequest request) {
    Optional<Person> person = personRepository.findByEmail(email);
    if(person.isPresent()){
      person.get().setEmail(request.getEmail());
      person.get().setName(request.getName());
      person.get().setLastname(request.getLastname());
      person.get().setPassword(request.getPassword());

      return personRepository.save(person.get());
    }else{
      throw new RuntimeException();
    }
  }

  @Override
  public Person get(String email) {
    Optional<Person> person = personRepository.findByEmail(email);
    if(person.isPresent()){
      return personRepository.findByEmail(email).get();
    }else{
      throw new RuntimeException();
    }
  }

  @Override
  public List<Person> getAll() {
    return personRepository.findAll();
  }

  @Override
  public void delete(String email) {
    personRepository.deleteByEmail(email);
  }

  @Override
  public List<Person> getFriends(String email) {
    Optional<Person> person = personRepository.findByEmail(email);
    if(person.isPresent()){
      return person.get().getFriends().stream().collect(Collectors.toList());
    }else{
      throw new RuntimeException();
    }
  }

  @Override
  public Person addFriend(String email, String friendEmail) {
    Optional<Person> person = personRepository.findByEmail(email);
    Optional<Person> friend = personRepository.findByEmail(friendEmail);
    if (person.isPresent() && friend.isPresent()) {
      person.get().getFriends().add(friend.get());
      return personRepository.save(person.get());
    } else {
      throw new RuntimeException();
    }
  }

  @Override
  public Person deleteFriend(String email, String friendEmail) {
    Optional<Person> person = personRepository.findByEmail(email);
    if (person.isPresent() ) {
      person.get().getFriends().removeIf(x -> x.getEmail().equals(friendEmail));
      return personRepository.save(person.get());
    } else {
      throw new RuntimeException();
    }  }

  @Override
  public Person getFriend(String email, String friendEmail) {
    Optional<Person> person = personRepository.findByEmail(email);
    if (person.isPresent() ) {
      Optional<Person> friend = person.get().getFriends().stream().filter(x -> x.getEmail().equals(friendEmail)).findFirst();
      if(friend.isPresent()) {
        return friend.get();
      } else {
        throw new RuntimeException();
      }
    } else {
      throw new RuntimeException();
    }
  }
}
