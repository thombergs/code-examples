package io.refectoring.cache.embedded.rest;


import io.refectoring.cache.embedded.HazelcastNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/cars")
public class Controller {

    private final HazelcastNode hazelcastNode;

    public Controller(HazelcastNode hazelcastNode) {
        this.hazelcastNode = hazelcastNode;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public String put(@RequestBody Car car){
        return hazelcastNode.put(car.getNumber(), car.getName());
    }

    @GetMapping(value = "/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Car get(@PathVariable String number){
        String value = hazelcastNode.get(number);
        return Car.builder()
                .number(number)
                .name(value)
                .build();
    }
}
