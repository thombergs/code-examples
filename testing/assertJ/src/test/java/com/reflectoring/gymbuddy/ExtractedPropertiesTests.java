package com.reflectoring.gymbuddy;

import com.reflectoring.gymbuddy.domain.Person;
import com.reflectoring.gymbuddy.domain.Session;
import com.reflectoring.gymbuddy.dto.person.PersonAddRequest;
import com.reflectoring.gymbuddy.dto.session.SessionAddRequest;
import com.reflectoring.gymbuddy.dto.set.SetAddRequest;
import com.reflectoring.gymbuddy.dto.workout.WorkoutAddRequest;
import com.reflectoring.gymbuddy.extractors.PersonExtractors;
import com.reflectoring.gymbuddy.services.PersonService;
import com.reflectoring.gymbuddy.services.SessionService;
import com.reflectoring.gymbuddy.services.SetService;
import com.reflectoring.gymbuddy.services.WorkoutService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ExtractedPropertiesTests {

    @Autowired
    PersonService personService;

    @Autowired
    SessionService sessionService;

    @Autowired
    WorkoutService workoutService;

    @Autowired
    SetService setService;


    @BeforeAll
    void init() {
        // Adding persons
        PersonAddRequest ironmanReq = new PersonAddRequest.PersonAddRequestBuilder()
                .name("Tony")
                .lastname("Stark")
                .email("tony.stark@avengers.com")
                .password("avengers")
                .build();
        PersonAddRequest hulkReq = new PersonAddRequest.PersonAddRequestBuilder()
                .name("Bruce")
                .lastname("Banner")
                .email("bruce.banner@avengers.com")
                .password("avengers")
                .build();
        PersonAddRequest marvelReq = new PersonAddRequest.PersonAddRequestBuilder()
                .name("Carol")
                .lastname("Danvers")
                .email("carol.danvers@avengers.com")
                .password("avengers")
                .build();
        PersonAddRequest widowReq = new PersonAddRequest.PersonAddRequestBuilder()
                .name("Natalia")
                .lastname("Romanova")
                .email("natalia.romanova@avengers.com")
                .password("avengers")
                .build();

        Person ironman = personService.add(ironmanReq);
        Person hulk = personService.add(hulkReq);
        Person marvel = personService.add(marvelReq);
        Person widow = personService.add(widowReq);

        // Adding friends to each person
        personService.addFriend(ironman.getEmail(), hulk.getEmail());
        personService.addFriend(ironman.getEmail(), widow.getEmail());

        personService.addFriend(hulk.getEmail(), widow.getEmail());
        personService.addFriend(hulk.getEmail(), marvel.getEmail());

        // Sets requests
        SetAddRequest pushupSetV1 = new SetAddRequest.SetAddRequestBuilder()
                .weight(0)
                .reps(50)
                .build();
        SetAddRequest pushupSetV2 = new SetAddRequest.SetAddRequestBuilder()
                .weight(0)
                .reps(25)
                .build();

        SetAddRequest pullupsSetV1 = new SetAddRequest.SetAddRequestBuilder()
                .weight(0)
                .reps(20)
                .build();
        SetAddRequest pullupsSetV2 = new SetAddRequest.SetAddRequestBuilder()
                .weight(0)
                .reps(25)
                .build();
        SetAddRequest pullupsSetV3 = new SetAddRequest.SetAddRequestBuilder()
                .weight(0)
                .reps(35)
                .build();
        SetAddRequest pullupsSetV4 = new SetAddRequest.SetAddRequestBuilder()
                .weight(0)
                .reps(5)
                .build();

        SetAddRequest squatsSetV1 = new SetAddRequest.SetAddRequestBuilder()
                .weight(120)
                .reps(20)
                .build();

        SetAddRequest deadliftsSetV1 = new SetAddRequest.SetAddRequestBuilder()
                .weight(80)
                .reps(40)
                .build();
        SetAddRequest deadliftsSetV2 = new SetAddRequest.SetAddRequestBuilder()
                .weight(150)
                .reps(20)
                .build();
        SetAddRequest deadliftsSetV3 = new SetAddRequest.SetAddRequestBuilder()
                .weight(250)
                .reps(5)
                .build();

        SetAddRequest hiitSetV1 = new SetAddRequest.SetAddRequestBuilder()
                .weight(0)
                .reps(5)
                .build();
        SetAddRequest hiitSetV2 = new SetAddRequest.SetAddRequestBuilder()
                .weight(0)
                .reps(50)
                .build();
        SetAddRequest hiitSetV3 = new SetAddRequest.SetAddRequestBuilder()
                .weight(0)
                .reps(25)
                .build();
        SetAddRequest hiitSetV4 = new SetAddRequest.SetAddRequestBuilder()
                .weight(50)
                .reps(40)
                .build();
        SetAddRequest hiitSetV5 = new SetAddRequest.SetAddRequestBuilder()
                .weight(100)
                .reps(5)
                .build();


        // Workout requests
        WorkoutAddRequest pushups = new WorkoutAddRequest.WorkoutAddRequestBuilder()
                .sets(List.of(pushupSetV1, pushupSetV1, pushupSetV2, pushupSetV1, pushupSetV2))
                .build();
        WorkoutAddRequest pullups = new WorkoutAddRequest.WorkoutAddRequestBuilder()
                .sets(List.of(pullupsSetV1, pullupsSetV2, pullupsSetV1, pullupsSetV4, pullupsSetV3))
                .build();
        WorkoutAddRequest squats = new WorkoutAddRequest.WorkoutAddRequestBuilder()
                .sets(List.of(squatsSetV1, squatsSetV1, squatsSetV1, squatsSetV1, squatsSetV1, squatsSetV1))
                .build();
        WorkoutAddRequest deadlifts = new WorkoutAddRequest.WorkoutAddRequestBuilder()
                .sets(List.of(deadliftsSetV1, deadliftsSetV2, deadliftsSetV1, deadliftsSetV2, deadliftsSetV3))
                .build();
        WorkoutAddRequest hiit = new WorkoutAddRequest.WorkoutAddRequestBuilder()
                .sets(List.of(hiitSetV1, hiitSetV2, hiitSetV3, hiitSetV4, hiitSetV5))
                .build();

        // Adding session to each
        SessionAddRequest ironmanSessionOne = new SessionAddRequest.SessionAddRequestBuilder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .workouts(List.of(pushups, pullups, squats))
                .build();
        SessionAddRequest ironmanSessionTwo = new SessionAddRequest.SessionAddRequestBuilder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(2).plusHours(3))
                .workouts(List.of(deadlifts, squats))
                .build();
        SessionAddRequest ironmanSessionThree = new SessionAddRequest.SessionAddRequestBuilder()
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(3).plusHours(2))
                .workouts(List.of(hiit))
                .build();

        SessionAddRequest hulkSessionOne = new SessionAddRequest.SessionAddRequestBuilder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(3))
                .workouts(List.of(squats, deadlifts))
                .build();
        SessionAddRequest hulkSessionTwo = new SessionAddRequest.SessionAddRequestBuilder()
                .start(LocalDateTime.now().minusDays(4))
                .end(LocalDateTime.now().minusDays(4).plusHours(2))
                .workouts(List.of(pullups, pushups, hiit))
                .build();

        SessionAddRequest marvelSessionOne = new SessionAddRequest.SessionAddRequestBuilder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .workouts(List.of(pushups, pullups, squats))
                .build();
        SessionAddRequest marvelSessionTwo = new SessionAddRequest.SessionAddRequestBuilder()
                .start(LocalDateTime.now().minusDays(5))
                .end(LocalDateTime.now().minusDays(5).plusHours(4))
                .workouts(List.of(deadlifts, squats))
                .build();
        SessionAddRequest marvelSessionThree = new SessionAddRequest.SessionAddRequestBuilder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusDays(1).plusHours(1))
                .workouts(List.of(hiit))
                .build();
        SessionAddRequest marvelSessionFour = new SessionAddRequest.SessionAddRequestBuilder()
                .start(LocalDateTime.now().minusDays(10))
                .end(LocalDateTime.now().minusDays(10).plusHours(5))
                .workouts(List.of(pushups, pullups, squats, deadlifts))
                .build();

        SessionAddRequest widowSessionOne = new SessionAddRequest.SessionAddRequestBuilder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(4))
                .workouts(List.of(hiit, squats))
                .build();

        // Adding sessions to persons
        sessionService.add(ironman, ironmanSessionOne);
        sessionService.add(ironman, ironmanSessionTwo);
        sessionService.add(ironman, ironmanSessionThree);

        sessionService.add(hulk, hulkSessionOne);
        sessionService.add(hulk, hulkSessionTwo);

        sessionService.add(marvel, marvelSessionOne);
        sessionService.add(marvel, marvelSessionTwo);
        sessionService.add(marvel, marvelSessionThree);
        sessionService.add(marvel, marvelSessionFour);

        sessionService.add(widow, widowSessionOne);
    }

    @Test
    void checkByName_UsingExtracting(){
        assertThat(personService.getAll())
                .extracting("name")
                .contains("Tony","Bruce","Carol","Natalia")
                .doesNotContain("Peter","Steve");
    }

    @Test
    void checkByNameAndLastname_UsingExtracting(){
        assertThat(personService.getAll())
                .extracting("name","lastname")
                .contains(tuple("Tony","Stark"), tuple("Carol", "Danvers"), tuple("Bruce", "Banner"),tuple("Natalia","Romanova"))
                .doesNotContain(tuple("Peter", "Parker"), tuple("Steve","Rogers"));
    }

    @Test
    void checkByNestedAtrribute_UsingExtracting(){
        assertThat(sessionService.getAll())
                .filteredOn(session -> session.getStart().isAfter(LocalDateTime.now().minusHours(1)))
                .extracting("person.name")
                .contains("Tony","Bruce","Carol","Natalia");
    }

    @Test
    void checkByNestedAtrribute_PersonIsNUll_UsingExtracting(){
        List<Session> sessions = sessionService.getAll().stream().map(
                session -> {
                    if(session.getPerson().getName().equals("Tony")){
                        return new Session.SessionBuilder()
                                .id(session.getId())
                                .start(session.getStart())
                                .end(session.getEnd())
                                .workouts(session.getWorkouts())
                                .person(null)
                                .build();
                    }
                    return session;
                }
        ).collect(Collectors.toList());

        assertThat(sessions)
                .filteredOn(session -> session.getStart().isAfter(LocalDateTime.now().minusHours(1)))
                .extracting("person.name")
                .contains("Bruce","Carol","Natalia");
    }

    // ----------- Flatmap extracting ---------

    @Test
    void filterOnAllSessionsThatAreFromToday_flatMapExtracting(){
        assertThat(personService.getAll()).flatExtracting("sessions")
                .filteredOn(session -> ((Session)session).getStart().isAfter(LocalDateTime.now().minusHours(1)))
                .extracting("person.name")
                .contains("Tony", "Carol","Bruce","Natalia");
    }

    @Test
    void filterOnAllSessionsThatAreFromToday_flatMapExtractingMethod(){
        assertThat(personService.getAll()).flatExtracting(PersonExtractors.sessions())
                .filteredOn(session -> session.getStart().isAfter(LocalDateTime.now().minusHours(1)))
                .extracting("person.name")
                .contains("Tony", "Carol","Bruce","Natalia");
    }

    // ----------- Method call extracting --------
    @Test
    void filterOnAllSesionThatAreFomToday_methodCallExtractingMethod(){
        assertThat(sessionService.getAll())
                .extractingResultOf("getDurationInMinutes", Long.class)
                .filteredOn(duration -> duration < 120l)
                .hasSize(1);
    }

}