package com.reflectoring.gymbuddy.conditions;

import com.reflectoring.gymbuddy.domain.Person;
import org.assertj.core.api.Condition;

import java.time.LocalDateTime;

public class SessionStartedTodayCondition extends Condition<Person> {

    @Override
    public boolean matches(Person person){
        return person.getSessions().stream()
                .anyMatch(session -> session.getStart().isAfter(LocalDateTime.now().minusHours(1)));
    }
}
