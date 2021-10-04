package com.reflectoring.gymbuddy.extractors;

import com.reflectoring.gymbuddy.domain.Person;
import com.reflectoring.gymbuddy.domain.Session;


import java.util.List;
import java.util.function.Function;

public class PersonExtractors {

    public PersonExtractors(){}

    public static Function<Person, List<Session>> sessions(){
        return new PersonSessionExtractor();
    }

    private static class PersonSessionExtractor implements Function<Person, List<Session>> {
        @Override
        public List<Session> apply(Person person) {
            return person.getSessions();
        }
    }
}
